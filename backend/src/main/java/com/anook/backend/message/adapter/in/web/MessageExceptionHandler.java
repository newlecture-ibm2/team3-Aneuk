package com.anook.backend.message.adapter.in.web;

import com.anook.backend.message.domain.exception.MessageThrottleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Message 도메인 예외 핸들러
 *
 * message 패키지 내 Controller에서 발생하는 예외를 처리
 */
@RestControllerAdvice(basePackages = "com.anook.backend.message")
public class MessageExceptionHandler {

    /**
     * 디바운스(연타 방지) 예외 → 429 Too Many Requests
     */
    @ExceptionHandler(MessageThrottleException.class)
    public ResponseEntity<Map<String, String>> handleThrottle(MessageThrottleException e) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Map.of("error", e.getMessage()));
    }

    /**
     * 잘못된 요청 (존재하지 않는 객실 등) → 400 Bad Request
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
    }
}
