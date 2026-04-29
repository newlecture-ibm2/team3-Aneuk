package com.anook.backend.request.application.port.in;

import com.anook.backend.request.application.dto.response.GetStaffTasksResult;

import java.util.List;

/**
 * 직원용 작업 목록 조회 UseCase
 */
public interface GetStaffTasksUseCase {

    /**
     * 필터 조건에 따라 직원용 작업 목록을 조회한다.
     *
     * @param status       상태 필터 (ALL이면 전체)
     * @param priority     우선순위 필터 (ALL이면 전체)
     * @param departmentId 부서 필터 (ALL이면 전체)
     * @return 직원용 작업 목록
     */
    List<GetStaffTasksResult> getTasks(String status, String priority, String departmentId);
}
