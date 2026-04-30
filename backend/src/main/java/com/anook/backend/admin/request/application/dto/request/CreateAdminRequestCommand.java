package com.anook.backend.admin.request.application.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 관리자 수동 요청 생성 Command
 */
public record CreateAdminRequestCommand(
        @NotBlank(message = "부서 ID는 필수입니다.")
        String departmentId,

        @NotBlank(message = "객실 번호는 필수입니다.")
        String roomNo,

        @NotBlank(message = "요약은 필수입니다.")
        String summary,

        String rawText,

        String priority,

        Long assignedStaffId
) {}
