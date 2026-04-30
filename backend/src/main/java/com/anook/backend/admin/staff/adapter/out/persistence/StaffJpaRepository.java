package com.anook.backend.admin.staff.adapter.out.persistence;

import com.anook.backend.admin.staff.adapter.out.persistence.entity.StaffJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Staff JPA Repository — admin/staff 모듈 전용
 */
public interface StaffJpaRepository extends JpaRepository<StaffJpaEntity, Long> {

    List<StaffJpaEntity> findByDepartmentId(String departmentId);
}
