package com.anook.backend.admin.department.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 부서 생성 Command DTO
 */
public record CreateDepartmentCommand(
        @NotBlank(message = "부서 코드는 필수입니다.")
        @Size(max = 20, message = "부서 코드는 20자 이내여야 합니다.")
        String id,

        @NotBlank(message = "부서 이름은 필수입니다.")
        @Size(max = 50, message = "부서 이름은 50자 이내여야 합니다.")
        String name
) {}
