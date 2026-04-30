package com.anook.backend.request.application.dto.response;

/**
 * 정산 완료 응답 DTO
 */
public record SettleRequestResult(
        Long id,
        String status
) {}
