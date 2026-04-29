package com.anook.backend.staff.request.application.service;

import com.anook.backend.staff.request.application.dto.response.GetStaffRequestsResult;
import com.anook.backend.staff.request.application.port.in.GetStaffRequestsUseCase;
import com.anook.backend.staff.request.application.port.out.StaffRequestRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * [직원 모듈] 요청 조회 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetStaffRequestsService implements GetStaffRequestsUseCase {

    private final StaffRequestRepositoryPort staffRequestRepository;

    @Override
    public List<GetStaffRequestsResult> getRequests(String status, String priority, String departmentId) {
        String s = "ALL".equalsIgnoreCase(status) ? null : status;
        String p = "ALL".equalsIgnoreCase(priority) ? null : priority;
        String d = "ALL".equalsIgnoreCase(departmentId) ? null : departmentId;
        
        return staffRequestRepository.findAllByFilters(s, p, d);
    }

    @Override
    public GetStaffRequestsResult getRequestDetail(Long id) {
        return staffRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("요청을 찾을 수 없습니다: " + id));
    }
}
