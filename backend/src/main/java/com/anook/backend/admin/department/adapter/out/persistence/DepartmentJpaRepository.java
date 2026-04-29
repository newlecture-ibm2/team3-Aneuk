package com.anook.backend.admin.department.adapter.out.persistence;

import com.anook.backend.admin.department.adapter.out.persistence.entity.DepartmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Department JPA Repository — admin/department 모듈 전용
 */
public interface DepartmentJpaRepository extends JpaRepository<DepartmentJpaEntity, String> {
}
