package com.anook.backend.guest.adapter.in.web;

import com.anook.backend.global.dto.ErrorResponse;
import com.anook.backend.guest.domain.exception.AlreadyCheckedInException;
import com.anook.backend.guest.domain.exception.GuestNotFoundException;
import com.anook.backend.guest.domain.exception.RoomNotFoundException;
import com.anook.backend.guest.domain.exception.UnsettledBillingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Guest 모듈 전용 예외 핸들러
 */
@RestControllerAdvice(basePackages = "com.anook.backend.guest")
public class GuestExceptionHandler {

    @ExceptionHandler(AlreadyCheckedInException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyCheckedIn(AlreadyCheckedInException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of("ALREADY_CHECKED_IN", e.getMessage()));
    }

    @ExceptionHandler(GuestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGuestNotFound(GuestNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of("GUEST_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoomNotFound(RoomNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of("ROOM_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(UnsettledBillingException.class)
    public ResponseEntity<ErrorResponse> handleUnsettledBilling(UnsettledBillingException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of("UNSETTLED_BILLING", e.getMessage()));
    }
}
