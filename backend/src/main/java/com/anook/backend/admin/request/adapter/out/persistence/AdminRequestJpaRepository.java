package com.anook.backend.admin.request.adapter.out.persistence;

import com.anook.backend.admin.request.adapter.out.persistence.entity.AdminRequestJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 관리자용 요청 JPA Repository
 */
public interface AdminRequestJpaRepository extends JpaRepository<AdminRequestJpaEntity, Long> {

    /**
     * 상태로 필터링
     */
    List<AdminRequestJpaEntity> findByStatusOrderByCreatedAtDesc(String status);

    /**
     * 부서로 필터링
     */
    List<AdminRequestJpaEntity> findByDepartmentIdOrderByCreatedAtDesc(String departmentId);

    /**
     * 전체 목록 (최신순)
     */
    List<AdminRequestJpaEntity> findAllByOrderByCreatedAtDesc();

    /**
     * 복합 필터 (JPQL)
     */
    @Query("SELECT r FROM AdminRequest r WHERE " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:departmentId IS NULL OR r.departmentId = :departmentId) AND " +
           "(:priority IS NULL OR r.priority = :priority) " +
           "ORDER BY r.createdAt DESC")
    List<AdminRequestJpaEntity> findAllWithFilters(
            @Param("status") String status,
            @Param("departmentId") String departmentId,
            @Param("priority") String priority
    );
}
