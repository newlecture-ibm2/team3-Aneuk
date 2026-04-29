package com.anook.backend.request.application.dto.response;

/**
 * WebSocket으로 전송될 알림 페이로드
 */
public record RequestWebSocketPayload(
        String eventType,     // "NEW_REQUEST", "STATUS_CHANGED", "ESCALATED"
        Long requestId,       // 요청 ID
        String status,        // 상태 (PENDING, IN_PROGRESS, COMPLETED 등)
        String domainCode,    // 부서 코드 (HK, FB 등)
        String summary,       // 요약
        String roomNo         // 객실 번호
) {
    /**
     * 신규 요청용 정적 팩토리
     */
    public static RequestWebSocketPayload newRequest(Long id, String status, String domainCode, String summary, String roomNo) {
        return new RequestWebSocketPayload("NEW_REQUEST", id, status, domainCode, summary, roomNo);
    }

    /**
     * 상태 변경용 정적 팩토리
     */
    public static RequestWebSocketPayload statusChanged(Long id, String status, String domainCode, String summary, String roomNo) {
        return new RequestWebSocketPayload("STATUS_CHANGED", id, status, domainCode, summary, roomNo);
    }
}
