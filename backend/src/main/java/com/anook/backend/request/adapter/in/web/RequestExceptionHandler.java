package com.anook.backend.request.adapter.in.web;

import com.anook.backend.global.dto.ErrorResponse;
import com.anook.backend.request.domain.exception.InvalidSettlementException;
import com.anook.backend.request.domain.exception.RequestNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Request 모듈 전용 예외 핸들러
 */
@RestControllerAdvice(basePackages = "com.anook.backend.request")
public class RequestExceptionHandler {

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRequestNotFound(RequestNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of("REQUEST_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(InvalidSettlementException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSettlement(InvalidSettlementException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of("INVALID_SETTLEMENT", e.getMessage()));
    }
}
