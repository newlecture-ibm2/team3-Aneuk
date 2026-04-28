package com.anook.backend.global.dto;

import java.time.LocalDateTime;

/**
 * 공통 에러 응답 형식
 */
public record ErrorResponse(
        String code,
        String message,
        LocalDateTime timestamp
) {
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, LocalDateTime.now());
    }
}
