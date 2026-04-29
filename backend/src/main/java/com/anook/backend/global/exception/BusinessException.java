package com.anook.backend.global.exception;

/**
 * 비즈니스 예외 — 모든 도메인 예외를 이 클래스 하나로 처리
 *
 * 사용법: throw new BusinessException(ErrorCode.ALREADY_CHECKED_IN);
 */
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String appendMessage) {
        super(errorCode.getMessage() + " " + appendMessage);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
