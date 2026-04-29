package com.anook.backend.guest.application.service;

import com.anook.backend.global.port.out.DispatchPort;
import com.anook.backend.guest.application.dto.request.CheckInGuestCommand;
import com.anook.backend.guest.application.dto.response.CheckInGuestResult;
import com.anook.backend.guest.application.port.in.CheckInGuestUseCase;
import com.anook.backend.guest.application.port.out.GuestRepositoryPort;
import com.anook.backend.guest.application.port.out.RoomQueryPort;
import com.anook.backend.global.exception.BusinessException;
import com.anook.backend.global.exception.ErrorCode;
import com.anook.backend.guest.domain.model.Guest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 투숙객 체크인 서비스
 *
 * ❌ JPA Repository 직접 import 금지
 * ✅ Port 인터페이스만 의존
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CheckInGuestService implements CheckInGuestUseCase {

    private final GuestRepositoryPort guestRepository;
    private final RoomQueryPort roomQueryPort;
    private final DispatchPort dispatchPort;

    @Override
    public CheckInGuestResult checkIn(CheckInGuestCommand command) {
        // 1) 객실 존재 여부 확인
        if (!roomQueryPort.existsById(command.roomId())) {
            throw new BusinessException(ErrorCode.ROOM_NOT_FOUND, "roomId=" + command.roomId());
        }

        // 2) 이미 해당 방에 투숙객이 있는지 확인
        if (guestRepository.existsByRoomId(command.roomId())) {
            throw new BusinessException(ErrorCode.ALREADY_CHECKED_IN);
        }

        // 3) 도메인 모델 생성 및 저장
        Guest guest = Guest.create(command.roomId(), command.guestName(), command.language(), command.checkoutDate());
        Guest saved = guestRepository.save(guest);

        // 4) roomNumber 조회
        String roomNumber = roomQueryPort.findRoomNumberById(saved.getRoomId());

        // 5) 관리자 채널로 체크인 알림 발송
        dispatchPort.sendToAdmin(Map.of(
                "type", "GUEST_CHECKED_IN",
                "roomNumber", roomNumber,
                "language", saved.getLanguage()
        ));

        return CheckInGuestResult.of(saved, roomNumber);
    }
}
