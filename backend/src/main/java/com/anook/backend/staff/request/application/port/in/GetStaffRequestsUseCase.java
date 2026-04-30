package com.anook.backend.staff.request.application.port.in;

import com.anook.backend.staff.request.adapter.in.web.dto.response.StaffTaskResult;

import java.util.List;

/**
 * 직원용 요청 목록 조회 UseCase
 */
public interface GetStaffRequestsUseCase {
    
    /**
     * 조건에 맞는 요청 목록을 조회합니다.
     * 
     * @param departmentId 부서 ID (ALL 이면 전체 조회)
     * @param status 상태 (ALL 이면 전체 조회)
     * @param priority 우선순위 (ALL 이면 전체 조회)
     * @return 요청 목록
     */
    List<StaffTaskResult> getRequests(String departmentId, String status, String priority);
}
