package com.anook.backend.request.application.port.out;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Request 영속성 포트
 */
public interface RequestRepositoryPort {

    /**
     * ID로 요청의 현재 상태 조회
     */
    Optional<RequestStatusDto> findStatusById(Long id);

    /**
     * 요청 상태 변경
     */
    void updateStatus(Long id, String status);

    /**
     * 필터 조건에 따라 작업 목록 조회 (room, staff 조인 포함)
     *
     * @param status       null이면 전체
     * @param priority     null이면 전체
     * @param departmentId null이면 전체
     */
    List<StaffTaskRow> findAllByFilters(String status, String priority, String departmentId);

    /**
     * 상태 조회 DTO (Port 레벨에서 사용하는 경량 DTO)
     */
    record RequestStatusDto(
            Long id,
            String status,
            String departmentId,
            String summary) {
    }

    /**
     * 직원용 작업 목록 조회 DTO (room, staff 조인 결과)
     */
    record StaffTaskRow(
            Long id,
            String status,
            String priority,
            String departmentId,
            String summary,
            String rawText,
            String roomNumber,
            String assignedStaffName,
            Float confidence,
            LocalDateTime createdAt) {
    }
}
