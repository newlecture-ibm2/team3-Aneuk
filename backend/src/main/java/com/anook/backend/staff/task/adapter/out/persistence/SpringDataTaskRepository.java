package com.anook.backend.staff.task.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA Repository — DB 레벨 필터링 (findAll().stream().filter() 금지)
 */
public interface SpringDataTaskRepository extends JpaRepository<RequestEntity, Long> {

    @Query("SELECT r FROM RequestEntity r JOIN FETCH r.room LEFT JOIN FETCH r.assignedStaff " +
           "WHERE (:status IS NULL OR r.status = :status) " +
           "AND (:priority IS NULL OR r.priority = :priority) " +
           "AND (:deptId IS NULL OR r.departmentId = :deptId) " +
           "ORDER BY r.createdAt DESC")
    List<RequestEntity> findAllByFilters(
            @Param("status") String status,
            @Param("priority") String priority,
            @Param("deptId") String deptId);
}
