package com.anook.backend.request.application.dto.response;

import java.time.LocalDateTime;

/**
 * 고객용 요청 내역 조회 결과 DTO
 */
public record GetMyRequestsResult(
        Long id,
        String status,
        String domainCode,
        String summary,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
