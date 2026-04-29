package com.anook.backend.staff.request.application.dto.response;

import java.time.LocalDateTime;

/**
 * [직원 모듈] 요청 조회 결과 DTO
 */
public record GetStaffRequestsResult(
        Long id,
        String status,
        String priority,
        String departmentId,
        String summary,
        String rawText,
        String roomNumber,
        String assignedStaffName,
        Float confidence,
        LocalDateTime createdAt
) {}
