package com.anook.backend.global.dto;

import java.time.LocalDateTime;

/**
 * 공통 에러 응답 형식
 *
 * - code: 에러 코드 (프론트 분기용)
 * - message: 사용자 노출 가능한 에러 메시지
 * - detail: 개발자용 상세 설명 (원인/해결 방법)
 * - timestamp: 에러 발생 시간
 */
public record ErrorResponse(
        String code,
        String message,
        String detail,
        LocalDateTime timestamp
) {
    public static ErrorResponse of(String code, String message, String detail) {
        return new ErrorResponse(code, message, detail, LocalDateTime.now());
    }

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, null, LocalDateTime.now());
    }
}
