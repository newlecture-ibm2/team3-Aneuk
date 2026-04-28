package com.anook.backend.guest.application.service;

import com.anook.backend.global.port.out.DispatchPort;
import com.anook.backend.guest.application.dto.request.CheckInGuestCommand;
import com.anook.backend.guest.application.dto.response.CheckInGuestResult;
import com.anook.backend.guest.application.dto.response.GetGuestResult;
import com.anook.backend.guest.application.port.in.CheckInGuestUseCase;
import com.anook.backend.guest.application.port.in.CheckOutGuestUseCase;
import com.anook.backend.guest.application.port.in.GetGuestUseCase;
import com.anook.backend.guest.application.port.out.GuestRepositoryPort;
import com.anook.backend.guest.application.port.out.RequestQueryPort;
import com.anook.backend.guest.application.port.out.RoomQueryPort;
import com.anook.backend.guest.domain.exception.AlreadyCheckedInException;
import com.anook.backend.guest.domain.exception.GuestNotFoundException;
import com.anook.backend.guest.domain.exception.RoomNotFoundException;
import com.anook.backend.guest.domain.exception.UnsettledBillingException;
import com.anook.backend.guest.domain.model.Guest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 투숙객 관리 서비스 (가상 PMS 핵심 로직)
 *
 * ❌ JPA Repository 직접 import 금지
 * ✅ Port 인터페이스만 의존
 */
@Service
@RequiredArgsConstructor
@Transactional
public class GuestService implements CheckInGuestUseCase, CheckOutGuestUseCase, GetGuestUseCase {

    private final GuestRepositoryPort guestRepository;
    private final RequestQueryPort requestQueryPort;
    private final RoomQueryPort roomQueryPort;
    private final DispatchPort dispatchPort;

    // ── 체크인 ──
    @Override
    public CheckInGuestResult checkIn(CheckInGuestCommand command) {
        // 1) 객실 존재 여부 확인
        if (!roomQueryPort.existsById(command.roomId())) {
            throw new RoomNotFoundException("존재하지 않는 객실입니다. roomId=" + command.roomId());
        }

        // 2) 이미 해당 방에 투숙객이 있는지 확인
        if (guestRepository.existsByRoomId(command.roomId())) {
            throw new AlreadyCheckedInException("해당 객실에 이미 투숙객이 있습니다.");
        }

        // 3) 도메인 모델 생성 및 저장
        Guest guest = Guest.create(command.roomId(), command.language());
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

    // ── 체크아웃 (Hard Delete) ──
    @Override
    public void checkOut(Long guestId) {
        // 1) 투숙객 존재 확인
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new GuestNotFoundException("투숙객을 찾을 수 없습니다. guestId=" + guestId));

        // 2) 미정산 F&B 건 확인
        boolean hasUnsettled = requestQueryPort
                .existsByRoomIdAndDepartmentIdAndStatusNotIn(
                        guest.getRoomId(), "FB", List.of("SETTLED", "CANCELLED")
                );

        if (hasUnsettled) {
            throw new UnsettledBillingException("미정산 F&B 내역이 있습니다. 결제를 먼저 완료해주세요.");
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

    // ── 투숙객 목록 조회 ──
    @Override
    @Transactional(readOnly = true)
    public List<GetGuestResult> getAll() {
        return guestRepository.findAll().stream()
                .map(guest -> GetGuestResult.of(
                        guest,
                        roomQueryPort.findRoomNumberById(guest.getRoomId())
                ))
                .toList();
    }
}
