package com.anook.backend.admin.request.adapter.out.persistence.entity;

import com.anook.backend.admin.request.domain.model.AdminRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 관리자용 요청 JPA 엔티티
 * — 같은 request 테이블을 바라보되, 엔티티 이름으로 분리
 */
@Entity(name = "AdminRequest")
@Table(name = "request")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminRequestJpaEntity {

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
    private Map<String, Object> entities;

    @Column(name = "raw_text")
    private String rawText;

    private String summary;

    private Float confidence;

    @Column(name = "room_no", nullable = false)
    private String roomNo;

    @Column(name = "assigned_staff_id")
    private Long assignedStaffId;

    @Column(nullable = false)
    private Integer version;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // === Domain 변환 ===

    public AdminRequest toDomain() {
        return new AdminRequest(
                this.id,
                this.status,
                this.priority,
                this.departmentId,
                this.entities,
                this.rawText,
                this.summary,
                this.confidence != null ? this.confidence : 0.0,
                this.roomNo,
                this.assignedStaffId,
                this.version,
                this.createdAt,
                this.updatedAt
        );
    }

    // === 쓰기 메서드 ===

    public void updateAssignedStaff(Long staffId) {
        this.assignedStaffId = staffId;
        if ("PENDING".equals(this.status)) {
            this.status = "ASSIGNED";
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePriority(String priority) {
        this.priority = priority;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = "CANCELLED";
        this.updatedAt = LocalDateTime.now();
    }
}
