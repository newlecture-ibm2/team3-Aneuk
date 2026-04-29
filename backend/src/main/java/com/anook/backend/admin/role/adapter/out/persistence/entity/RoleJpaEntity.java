package com.anook.backend.admin.role.adapter.out.persistence.entity;

import com.anook.backend.admin.role.domain.model.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "AdminRole")
@Table(name = "staff_role")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    private RoleJpaEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role toDomain() {
        return new Role(id, name);
    }

    public static RoleJpaEntity fromDomain(Role role) {
        return new RoleJpaEntity(role.getId(), role.getName());
    }
}
