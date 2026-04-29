package com.anook.backend.request.application.service;

import com.anook.backend.request.application.dto.response.GetStaffTasksResult;
import com.anook.backend.request.application.port.in.GetStaffTasksUseCase;
import com.anook.backend.request.application.port.out.RequestRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 직원용 작업 목록 조회 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetStaffTasksService implements GetStaffTasksUseCase {

    private final RequestRepositoryPort requestRepository;

    @Override
    public List<GetStaffTasksResult> getTasks(String status, String priority, String departmentId) {
        // "ALL"이면 null로 변환 → 전체 조회
        String statusFilter = "ALL".equalsIgnoreCase(status) ? null : status;
        String priorityFilter = "ALL".equalsIgnoreCase(priority) ? null : priority;
        String deptFilter = "ALL".equalsIgnoreCase(departmentId) ? null : departmentId;

        return requestRepository.findAllByFilters(statusFilter, priorityFilter, deptFilter)
                .stream()
                .map(row -> new GetStaffTasksResult(
                        row.id(),
                        row.status(),
                        row.priority(),
                        row.departmentId(),
                        row.summary(),
                        row.rawText(),
                        row.roomNumber(),
                        row.assignedStaffName(),
                        row.confidence(),
                        row.createdAt()
                ))
                .toList();
    }
}
