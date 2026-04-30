package com.anook.backend.admin.request.application.port.in;

import com.anook.backend.admin.request.application.dto.request.AssignRequestCommand;
import com.anook.backend.admin.request.application.dto.request.ChangeRequestPriorityCommand;
import com.anook.backend.admin.request.application.dto.response.AdminRequestDetailResult;
import com.anook.backend.admin.request.application.dto.response.AdminRequestListResult;

import java.util.List;

/**
 * 관리자 요청 관리 UseCase
 */
public interface ManageAdminRequestUseCase {

    /**
     * 전체 요청 목록 조회 (필터링 + 정렬)
     */
    List<AdminRequestListResult> getAllRequests(String status, String departmentId, String priority, String sort);

    /**
     * 단건 요청 상세 조회
     */
    AdminRequestDetailResult getRequestDetail(Long id);

    /**
     * 담당자 배정/재배정
     */
    void assignRequest(Long id, AssignRequestCommand command);

    /**
     * 우선순위 변경
     */
    void changeRequestPriority(Long id, ChangeRequestPriorityCommand command);

    /**
     * 요청 취소 (관리자 권한)
     */
    void cancelRequest(Long id);
}
