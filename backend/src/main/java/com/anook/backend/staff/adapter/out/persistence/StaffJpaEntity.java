package com.anook.backend.staff.adapter.out.persistence;

import com.anook.backend.staff.domain.model.Staff;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "staff")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StaffJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String pin;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentJpaEntity department;

    public Staff toDomain() {
        return Staff.builder()
                .id(id)
                .name(name)
                .pin(pin)
                .roleId(roleId)
                .department(department.toDomain())
                .build();
    }
}
