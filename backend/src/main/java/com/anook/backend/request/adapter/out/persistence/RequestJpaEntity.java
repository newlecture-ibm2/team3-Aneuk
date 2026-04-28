package com.anook.backend.request.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * Request JPA Entity — request 모듈 전용
 */
@Entity
@Table(name = "request")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String priority;

    @Column(name = "department_id", nullable = false)
    private String departmentId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String entities;

    @Column(name = "raw_text")
    private String rawText;

    private String summary;

    private Float confidence;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "assigned_staff_id")
    private Long assignedStaffId;

    @Column(nullable = false)
    private Integer version;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // === 상태 변경 ===
    public void updateStatus(String newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }
}
