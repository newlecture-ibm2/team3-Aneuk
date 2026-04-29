package com.anook.backend.message.domain.exception;

/**
 * 메시지 디바운스 예외
 *
 * 같은 객실에서 너무 빠르게 메시지를 연속 전송할 때 발생
 */
public class MessageThrottleException extends RuntimeException {

    public MessageThrottleException(String message) {
        super(message);
    }
}
