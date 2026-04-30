package com.anook.backend.message.domain.model;

/**
 * 메시지 발신자 유형
 *
 * GUEST  - 투숙객이 보낸 메시지
 * AI     - AI 컨시어지가 생성한 응답
 * STAFF  - 직원이 직접 보낸 메시지 (에스컬레이션 시)
 */
public enum SenderType {
    GUEST,
    AI,
    STAFF
}
