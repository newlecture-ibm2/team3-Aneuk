package com.anook.backend.admin.department.adapter.out.persistence.entity;

import com.anook.backend.admin.department.domain.model.Department;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 관리자용 부서 JPA 엔티티
 * — 같은 department 테이블을 바라보되, 엔티티 이름으로 분리
 */
@Entity(name = "AdminDepartment")
@Table(name = "department")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepartmentJpaEntity {

    @Id
    @Column(length = 20)
    private String id;

    @Column(nullable = false, length = 50)
    private String name;

    private DepartmentJpaEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // === Entity → Domain ===
    public Department toDomain() {
        return new Department(id, name);
    }

    // === Domain → Entity ===
    public static DepartmentJpaEntity fromDomain(Department department) {
        return new DepartmentJpaEntity(department.getId(), department.getName());
    }
}
