package com.anook.backend.request.domain.exception;

/**
 * 정산 불가 상태에서 정산 시도 시 발생
 * (예: COMPLETED가 아닌 상태에서 정산 시도, FB 부서가 아닌 요청 정산 시도)
 */
public class InvalidSettlementException extends RuntimeException {
    public InvalidSettlementException(String message) {
        super(message);
    }
}
