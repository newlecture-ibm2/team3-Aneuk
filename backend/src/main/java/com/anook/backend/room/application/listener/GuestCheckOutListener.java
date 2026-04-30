package com.anook.backend.room.application.listener;

import com.anook.backend.guest.domain.event.GuestCheckedOutEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 체크아웃 이벤트 리스너 (ANOOK 측)
 *
 * PMS에서 체크아웃이 발생하면 해당 객실의 QR 인증 세션을 무효화한다.
 * 향후 WebSocket을 통해 해당 객실 브라우저에 세션 만료 알림을 보낼 수 있다.
 */
@Component
@Slf4j
public class GuestCheckOutListener {

    @EventListener
    public void onGuestCheckedOut(GuestCheckedOutEvent event) {
        String roomNumber = event.roomNumber();

        // TODO: QR 인증 세션 무효화 로직 (예: 세션 스토어에서 roomNumber 제거)
        // TODO: WebSocket으로 해당 방 브라우저에 세션 만료 알림 전송
        //       messagingTemplate.convertAndSend("/topic/room/" + roomNumber, "SESSION_EXPIRED");

        log.info("[ANOOK] {}호 체크아웃 감지 → QR 세션 무효화 처리", roomNumber);
    }
}
