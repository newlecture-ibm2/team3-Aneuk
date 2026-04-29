package com.anook.backend.admin.department.domain.model;

import java.util.Objects;

/**
 * 부서 도메인 모델 — 순수 POJO (JPA, Spring 의존 금지)
 *
 * DB: department 테이블 (id VARCHAR(20) PK, name VARCHAR(50))
 * 예시: id="HK", name="하우스키핑" / id="FD", name="프론트데스크" / id="FB", name="F&B"
 */
public class Department {

    private final String id;
    private final String name;

    public Department(String id, String name) {
        this.id = Objects.requireNonNull(id, "부서 코드(id)는 필수입니다.");
        this.name = Objects.requireNonNull(name, "부서 이름은 필수입니다.");
    }

    public String getId() { return id; }
    public String getName() { return name; }
}
