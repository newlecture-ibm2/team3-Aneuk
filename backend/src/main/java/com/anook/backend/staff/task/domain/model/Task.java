package com.anook.backend.staff.task.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 직원 태스크 도메인 모델 — request 테이블의 비즈니스 표현
 */
@Getter
@Builder
public class Task {
    private final Long id;
    private final String status;        // PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
    private final String priority;      // LOW, NORMAL, HIGH, URGENT
    private final String departmentId;  // HK, FB, FACILITY, CONCIERGE, FRONT, EMERGENCY
    private final String summary;       // AI 요약 (대시보드 카드 제목)
    private final String rawText;       // 고객 발화 원문
    private final String roomNumber;    // 객실 번호
    private final String assignedStaffName;
    private final Float confidence;     // AI 확신도
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
