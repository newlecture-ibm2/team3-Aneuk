package com.anook.backend.admin.department.application.dto.response;

import com.anook.backend.admin.department.domain.model.Department;

/**
 * 부서 조회 Result DTO
 */
public record GetDepartmentResult(
        String id,
        String name
) {
    public static GetDepartmentResult from(Department department) {
        return new GetDepartmentResult(department.getId(), department.getName());
    }
}
