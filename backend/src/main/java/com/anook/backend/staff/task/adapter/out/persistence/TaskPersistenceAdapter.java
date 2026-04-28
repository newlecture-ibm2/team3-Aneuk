package com.anook.backend.staff.task.adapter.out.persistence;

import com.anook.backend.staff.task.application.port.out.TaskRepositoryPort;
import com.anook.backend.staff.task.domain.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 영속성 어댑터 — Entity ↔ Domain 매핑 담당
 */
@Component
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements TaskRepositoryPort {

    private final SpringDataTaskRepository repository;

    @Override
    public List<Task> findAllByFilters(String status, String priority, String departmentId) {
        return repository.findAllByFilters(status, priority, departmentId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Task toDomain(RequestEntity entity) {
        return Task.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .priority(entity.getPriority())
                .departmentId(entity.getDepartmentId())
                .summary(entity.getSummary() != null ? entity.getSummary() : "일반 요청")
                .rawText(entity.getRawText())
                .roomNumber(entity.getRoom().getNumber())
                .assignedStaffName(
                        entity.getAssignedStaff() != null
                                ? entity.getAssignedStaff().getName()
                                : null)
                .confidence(entity.getConfidence())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
