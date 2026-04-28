package com.anook.backend.guest.domain.exception;

/**
 * 미정산 F&B 내역이 있는 상태에서 체크아웃 시도 시 발생
 */
public class UnsettledBillingException extends RuntimeException {
    public UnsettledBillingException(String message) {
        super(message);
    }
}
