package com.anook.backend.staff.task.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * staff 테이블 엔티티 — staff/task 모듈 전용
 */
@Entity
@Table(name = "staff")
@Getter
@NoArgsConstructor
public class StaffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String pin;

    @Column(name = "role_id", nullable = false)
    private String roleId;

    @Column(name = "department_id", nullable = false)
    private String departmentId;
}
