package com.anook.backend.guest.domain.exception;

/**
 * 존재하지 않는 guestId로 조회/체크아웃 시도 시 발생
 */
public class GuestNotFoundException extends RuntimeException {
    public GuestNotFoundException(String message) {
        super(message);
    }
}
