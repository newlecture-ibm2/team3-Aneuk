package com.anook.backend.guest.domain.event;

/**
 * 투숙객 체크아웃 이벤트
 *
 * 체크아웃 완료 후 발행되어, ANOOK 측에서 해당 객실의
 * QR 인증 세션을 무효화하는 데 사용된다.
 */
public record GuestCheckedOutEvent(
        String roomNumber
) {}
