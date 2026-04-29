package com.anook.backend.guest.application.service;

import com.anook.backend.global.port.out.DispatchPort;
import com.anook.backend.guest.application.port.in.CheckOutGuestUseCase;
import com.anook.backend.guest.application.port.out.GuestRepositoryPort;
import com.anook.backend.guest.application.port.out.RequestQueryPort;
import com.anook.backend.guest.application.port.out.RoomQueryPort;
import com.anook.backend.global.exception.BusinessException;
import com.anook.backend.global.exception.ErrorCode;
import com.anook.backend.guest.domain.model.Guest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 투숙객 체크아웃 서비스 (Hard Delete — PII 최소화)
 *
 * ❌ JPA Repository 직접 import 금지
 * ✅ Port 인터페이스만 의존
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CheckOutGuestService implements CheckOutGuestUseCase {

    private final GuestRepositoryPort guestRepository;
    private final RequestQueryPort requestQueryPort;
    private final RoomQueryPort roomQueryPort;
    private final DispatchPort dispatchPort;

    @Override
    public void checkOut(Long guestId) {
        // 1) 투숙객 존재 확인
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GUEST_NOT_FOUND, "guestId=" + guestId));

        // 2) 미정산 F&B 건 확인
        boolean hasUnsettled = requestQueryPort
                .existsByRoomIdAndDepartmentIdAndStatusNotIn(
                        guest.getRoomId(), "FB", List.of("SETTLED", "CANCELLED")
                );

        if (hasUnsettled) {
            throw new BusinessException(ErrorCode.UNSETTLED_BILLING);
        }

        // 3) roomNumber 조회 (삭제 전에 미리 가져옴)
        String roomNumber = roomQueryPort.findRoomNumberById(guest.getRoomId());

        // 4) Hard Delete
        guestRepository.deleteById(guestId);

        // 5) 객실 채널로 세션 종료 이벤트 발송 (프론트에서 채팅 비활성화)
        dispatchPort.sendToRoom(roomNumber, Map.of(
                "type", "SESSION_TERMINATED",
                "message", "체크아웃이 완료되었습니다."
        ));

        // 6) 관리자 채널로 체크아웃 알림 발송
        dispatchPort.sendToAdmin(Map.of(
                "type", "GUEST_CHECKED_OUT",
                "roomNumber", roomNumber
        ));
    }
}
