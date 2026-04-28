package com.anook.backend.staff.task.application.service;

import com.anook.backend.staff.task.application.port.in.GetStaffTasksUseCase;
import com.anook.backend.staff.task.application.port.out.TaskRepositoryPort;
import com.anook.backend.staff.task.application.result.GetStaffTasksResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetStaffTasksService implements GetStaffTasksUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public List<GetStaffTasksResult> getTasks(String status, String priority, String departmentId) {
        String statusFilter = "ALL".equals(status) ? null : status;
        String priorityFilter = "ALL".equals(priority) ? null : priority;
        String deptFilter = "ALL".equals(departmentId) ? null : departmentId;

        return taskRepositoryPort.findAllByFilters(statusFilter, priorityFilter, deptFilter).stream()
                .map(GetStaffTasksResult::from)
                .collect(Collectors.toList());
    }
}
