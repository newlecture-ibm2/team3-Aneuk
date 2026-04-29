package com.anook.backend.guest.application.service;

import com.anook.backend.guest.application.port.in.CheckOutGuestUseCase;
import com.anook.backend.guest.application.port.out.GuestRepositoryPort;
import com.anook.backend.guest.application.port.out.RequestQueryPort;
import com.anook.backend.global.exception.BusinessException;
import com.anook.backend.global.exception.ErrorCode;
import com.anook.backend.guest.domain.model.Guest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * PMS 투숙객 체크아웃 서비스 (Hard Delete)
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

    @Override
    public void checkOut(Long guestId) {
        // 1) 투숙객 존재 확인
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GUEST_NOT_FOUND, "guestId=" + guestId));

        // 2) 미정산 F&B 건 확인
        boolean hasUnsettled = requestQueryPort
                .existsByRoomNumberAndDepartmentIdAndStatusNotIn(
                        guest.getRoomNumber(), "FB", List.of("SETTLED", "CANCELLED")
                );

        if (hasUnsettled) {
            throw new BusinessException(ErrorCode.UNSETTLED_BILLING);
        }

        // 3) Hard Delete
        guestRepository.deleteById(guestId);
    }
}
