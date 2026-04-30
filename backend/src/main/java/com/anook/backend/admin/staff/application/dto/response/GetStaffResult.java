package com.anook.backend.admin.staff.application.dto.response;

import com.anook.backend.admin.staff.domain.model.Staff;

public record GetStaffResult(
        Long id,
        String name,
        String pin,
        Long roleId,
        String departmentId
) {
    public static GetStaffResult from(Staff staff) {
        return new GetStaffResult(
                staff.getId(),
                staff.getName(),
                staff.getPin(),
                staff.getRoleId(),
                staff.getDepartmentId()
        );
    }
}
