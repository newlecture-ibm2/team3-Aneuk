package com.anook.backend.request.domain.exception;

/**
 * 존재하지 않는 요청 ID로 조회 시 발생
 */
public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(String message) {
        super(message);
    }
}
