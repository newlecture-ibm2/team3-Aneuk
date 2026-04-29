package com.anook.backend.admin.department.adapter.out.persistence;

import com.anook.backend.admin.department.adapter.out.persistence.entity.DepartmentJpaEntity;
import com.anook.backend.admin.department.application.port.out.DepartmentRepositoryPort;
import com.anook.backend.admin.department.domain.model.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * DepartmentRepositoryPort 구현체 — Department 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class DepartmentPersistenceAdapter implements DepartmentRepositoryPort {

    private final DepartmentJpaRepository jpaRepository;

    @Override
    public Optional<Department> findById(String id) {
        return jpaRepository.findById(id).map(DepartmentJpaEntity::toDomain);
    }

    @Override
    public List<Department> findAll() {
        return jpaRepository.findAll().stream()
                .map(DepartmentJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Department save(Department department) {
        DepartmentJpaEntity entity = DepartmentJpaEntity.fromDomain(department);
        DepartmentJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return jpaRepository.existsById(id);
    }
}
