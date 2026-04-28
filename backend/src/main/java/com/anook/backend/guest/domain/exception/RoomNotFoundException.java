package com.anook.backend.guest.domain.exception;

/**
 * 존재하지 않는 roomId로 체크인 시도 시 발생
 */
public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(String message) {
        super(message);
    }
}
