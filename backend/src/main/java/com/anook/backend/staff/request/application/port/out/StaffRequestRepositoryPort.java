package com.anook.backend.staff.request.application.port.out;

import com.anook.backend.staff.request.application.dto.response.GetStaffRequestsResult;
import java.util.List;
import java.util.Optional;

/**
 * [직원 모듈] 요청 전용 영속성 포트
 * 
 * 규칙: 타 모듈(request)의 데이터를 조회할 때 본인 모듈만의 포트를 정의함.
 */
public interface StaffRequestRepositoryPort {
    List<GetStaffRequestsResult> findAllByFilters(String status, String priority, String departmentId);
    Optional<GetStaffRequestsResult> findById(Long id);

    /**
     * [Staff 전용] 요청 상태 업데이트
     */
    void updateStatus(Long requestId, String status);

    /**
     * [Staff 전용] 요청 상태 및 담당자 업데이트
     */
    void updateStatusWithStaff(Long requestId, String status, String staffName);
}
