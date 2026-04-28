package com.anook.backend.guest.domain.exception;

/**
 * 이미 체크인된 객실에 재체크인 시도 시 발생
 */
public class AlreadyCheckedInException extends RuntimeException {
    public AlreadyCheckedInException(String message) {
        super(message);
    }
}
