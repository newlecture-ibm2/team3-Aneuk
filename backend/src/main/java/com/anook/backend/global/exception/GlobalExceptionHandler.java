package com.anook.backend.global.exception;

import com.anook.backend.global.dto.ErrorResponse;
import com.anook.backend.guest.domain.exception.AlreadyCheckedInException;
import com.anook.backend.guest.domain.exception.GuestNotFoundException;
import com.anook.backend.guest.domain.exception.RoomNotFoundException;
import com.anook.backend.guest.domain.exception.UnsettledBillingException;
import com.anook.backend.request.domain.exception.InvalidSettlementException;
import com.anook.backend.request.domain.exception.RequestNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 핸들러 — 모든 모듈의 도메인 예외를 한 곳에서 처리
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── 404 NOT_FOUND ──

    @ExceptionHandler(GuestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGuestNotFound(GuestNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                        "GUEST_NOT_FOUND",
                        e.getMessage(),
                        "해당 ID의 투숙객이 존재하지 않습니다. 이미 체크아웃(Hard Delete)되었거나, 잘못된 guestId입니다. GET /admin/guests로 현재 투숙객 목록을 확인하세요."
                ));
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoomNotFound(RoomNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                        "ROOM_NOT_FOUND",
                        e.getMessage(),
                        "해당 ID의 객실이 존재하지 않습니다. GET /admin/rooms로 유효한 객실 목록을 확인하세요."
                ));
    }

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRequestNotFound(RequestNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                        "REQUEST_NOT_FOUND",
                        e.getMessage(),
                        "해당 ID의 요청이 존재하지 않습니다. 올바른 taskId를 확인하세요."
                ));
    }

    // ── 409 CONFLICT ──

    @ExceptionHandler(AlreadyCheckedInException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyCheckedIn(AlreadyCheckedInException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(
                        "ALREADY_CHECKED_IN",
                        e.getMessage(),
                        "해당 객실에 이미 투숙 중인 게스트가 있습니다. 기존 투숙객을 체크아웃(DELETE /admin/guests/{id})한 후 다시 시도하세요."
                ));
    }

    @ExceptionHandler(UnsettledBillingException.class)
    public ResponseEntity<ErrorResponse> handleUnsettledBilling(UnsettledBillingException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(
                        "UNSETTLED_BILLING",
                        e.getMessage(),
                        "해당 객실에 정산되지 않은 F&B 요청이 있습니다. PATCH /admin/tasks/{taskId}/settle로 모든 F&B 요청을 정산한 후 체크아웃하세요."
                ));
    }

    // ── 400 BAD_REQUEST ──

    @ExceptionHandler(InvalidSettlementException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSettlement(InvalidSettlementException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(
                        "INVALID_SETTLEMENT",
                        e.getMessage(),
                        "정산은 department_id가 'FB'이고 status가 'COMPLETED'인 요청에만 가능합니다. 현재 요청의 상태와 부서를 확인하세요."
                ));
    }
}
