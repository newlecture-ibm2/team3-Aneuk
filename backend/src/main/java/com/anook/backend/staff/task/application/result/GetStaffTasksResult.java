package com.anook.backend.staff.task.application.result;

import com.anook.backend.staff.task.domain.model.Task;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 직원 대시보드용 응답 DTO
 */
@Getter
@Builder
public class GetStaffTasksResult {
    private final Long id;
    private final String status;
    private final String priority;
    private final String departmentId;
    private final String summary;
    private final String rawText;
    private final String roomNumber;
    private final String assignedStaffName;
    private final Float confidence;
    private final LocalDateTime createdAt;

    public static GetStaffTasksResult from(Task task) {
        return GetStaffTasksResult.builder()
                .id(task.getId())
                .status(task.getStatus())
                .priority(task.getPriority())
                .departmentId(task.getDepartmentId())
                .summary(task.getSummary())
                .rawText(task.getRawText())
                .roomNumber(task.getRoomNumber())
                .assignedStaffName(task.getAssignedStaffName())
                .confidence(task.getConfidence())
                .createdAt(task.getCreatedAt())
                .build();
    }
}
