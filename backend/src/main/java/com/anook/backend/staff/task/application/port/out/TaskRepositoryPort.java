package com.anook.backend.staff.task.application.port.out;

import com.anook.backend.staff.task.domain.model.Task;

import java.util.List;

/**
 * 태스크 조회 아웃바운드 포트 — Domain 모델만 반환
 */
public interface TaskRepositoryPort {
    List<Task> findAllByFilters(String status, String priority, String departmentId);
}
