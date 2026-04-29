package com.anook.backend.request.application.dto.response;

import java.time.LocalDateTime;

/**
 * 직원용 작업 목록 응답 DTO
 */
public record GetStaffTasksResult(
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
