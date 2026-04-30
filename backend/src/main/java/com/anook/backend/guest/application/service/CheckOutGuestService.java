package com.anook.backend.guest.application.service;

import com.anook.backend.guest.application.port.in.CheckOutGuestUseCase;
import com.anook.backend.guest.application.port.out.GuestRepositoryPort;
import com.anook.backend.guest.application.port.out.ReceiptQueryPort;
import com.anook.backend.guest.application.port.out.RequestQueryPort;
import com.anook.backend.guest.domain.event.GuestCheckedOutEvent;
import com.anook.backend.global.exception.BusinessException;
import com.anook.backend.global.exception.ErrorCode;
import com.anook.backend.guest.domain.model.Guest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * PMS 투숙객 체크아웃 서비스 (Hard Delete)
 *
 * 체크아웃 전 다음 조건을 검증한다:
 * 1) 투숙객 존재 확인
 * 2) 미정산 F&B request 확인 (request 테이블)
 * 3) 미결제 룸서비스 영수증 확인 (pms_receipt 테이블)
 *
 * ❌ JPA Repository 직접 import 금지
 * ✅ Port 인터페이스만 의존
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CheckOutGuestService implements CheckOutGuestUseCase {

    private final GuestRepositoryPort guestRepository;
    private final RequestQueryPort requestQueryPort;
    private final ReceiptQueryPort receiptQueryPort;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void checkOut(Long guestId) {
        // 1) 투숙객 존재 확인
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GUEST_NOT_FOUND, "guestId=" + guestId));

        // 2) 미정산 F&B 건 확인 (request 테이블)
        boolean hasUnsettled = requestQueryPort
                .existsByRoomNumberAndDepartmentIdAndStatusNotIn(
                        guest.getRoomNumber(), "FB", List.of("SETTLED", "CANCELLED")
                );

        if (hasUnsettled) {
            throw new BusinessException(ErrorCode.UNSETTLED_BILLING);
        }

        // 3) 미결제 룸서비스 영수증 확인 (pms_receipt 테이블)
        if (receiptQueryPort.hasUnpaidReceipts(guest.getRoomNumber())) {
            throw new BusinessException(ErrorCode.UNPAID_RECEIPTS_EXIST);
        }

        String roomNumber = guest.getRoomNumber();

        // 4) Hard Delete
        guestRepository.deleteById(guestId);

        // 5) 체크아웃 이벤트 발행 → ANOOK 세션 무효화
        eventPublisher.publishEvent(new GuestCheckedOutEvent(roomNumber));
        log.info("[CheckOut] {}호 체크아웃 완료 → 이벤트 발행", roomNumber);
    }
}

