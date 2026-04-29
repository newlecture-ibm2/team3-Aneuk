package com.anook.backend.admin.staff.domain.model;

import java.util.Objects;

/**
 * 직원 도메인 모델 — 순수 POJO (JPA, Spring 의존 금지)
 *
 * DB: staff 테이블 (id BIGSERIAL PK, name, pin, role_id FK, department_id FK)
 * 부서/역할은 ID 참조로 결합도 최소화
 */
public class Staff {

    private final Long id;
    private final String name;
    private final String pin;
    private final String roleId;
    private final String departmentId;

    public Staff(Long id, String name, String pin, String roleId, String departmentId) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "직원 이름은 필수입니다.");
        this.pin = Objects.requireNonNull(pin, "PIN 코드는 필수입니다.");
        this.roleId = Objects.requireNonNull(roleId, "역할 ID는 필수입니다.");
        this.departmentId = Objects.requireNonNull(departmentId, "부서 ID는 필수입니다.");
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getPin() { return pin; }
    public String getRoleId() { return roleId; }
    public String getDepartmentId() { return departmentId; }
}
