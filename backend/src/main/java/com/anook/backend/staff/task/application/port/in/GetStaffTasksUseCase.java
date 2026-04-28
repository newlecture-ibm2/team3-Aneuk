package com.anook.backend.staff.task.application.port.in;

import com.anook.backend.staff.task.application.result.GetStaffTasksResult;

import java.util.List;

/**
 * 직원 태스크 목록 조회 유스케이스
 */
public interface GetStaffTasksUseCase {
    List<GetStaffTasksResult> getTasks(String status, String priority, String departmentId);
}
