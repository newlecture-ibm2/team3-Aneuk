package com.anook.backend.admin.department.application.port.out;

import com.anook.backend.admin.department.domain.model.Department;

import java.util.List;
import java.util.Optional;

/**
 * Department 영속성 포트 — 도메인 모델만 반환
 */
public interface DepartmentRepositoryPort {

    Optional<Department> findById(String id);

    List<Department> findAll();

    Department save(Department department);

    void deleteById(String id);

    boolean existsById(String id);
}
