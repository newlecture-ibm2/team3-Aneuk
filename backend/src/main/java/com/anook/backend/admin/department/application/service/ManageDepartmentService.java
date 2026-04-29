package com.anook.backend.admin.department.application.service;

import com.anook.backend.admin.department.application.dto.request.CreateDepartmentCommand;
import com.anook.backend.admin.department.application.dto.request.UpdateDepartmentCommand;
import com.anook.backend.admin.department.application.dto.response.GetDepartmentResult;
import com.anook.backend.admin.department.application.port.in.ManageDepartmentUseCase;
import com.anook.backend.admin.department.application.port.out.DepartmentRepositoryPort;
import com.anook.backend.admin.department.domain.model.Department;
import com.anook.backend.global.exception.BusinessException;
import com.anook.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 부서 관리 서비스 — ManageDepartmentUseCase 구현체
 *
 * Port(Out)만 의존, JPA Repository 직접 import 금지
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManageDepartmentService implements ManageDepartmentUseCase {

    private final DepartmentRepositoryPort departmentRepositoryPort;

    @Override
    @Transactional
    public GetDepartmentResult create(CreateDepartmentCommand command) {
        if (departmentRepositoryPort.existsById(command.id())) {
            throw new BusinessException(ErrorCode.DEPARTMENT_ALREADY_EXISTS);
        }

        Department department = new Department(command.id(), command.name());
        Department saved = departmentRepositoryPort.save(department);
        return GetDepartmentResult.from(saved);
    }

    @Override
    public List<GetDepartmentResult> getAll() {
        return departmentRepositoryPort.findAll().stream()
                .map(GetDepartmentResult::from)
                .toList();
    }

    @Override
    public GetDepartmentResult getById(String id) {
        Department department = departmentRepositoryPort.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.DEPARTMENT_NOT_FOUND));
        return GetDepartmentResult.from(department);
    }

    @Override
    @Transactional
    public GetDepartmentResult update(String id, UpdateDepartmentCommand command) {
        if (!departmentRepositoryPort.existsById(id)) {
            throw new BusinessException(ErrorCode.DEPARTMENT_NOT_FOUND);
        }

        Department updated = new Department(id, command.name());
        Department saved = departmentRepositoryPort.save(updated);
        return GetDepartmentResult.from(saved);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!departmentRepositoryPort.existsById(id)) {
            throw new BusinessException(ErrorCode.DEPARTMENT_NOT_FOUND);
        }
        departmentRepositoryPort.deleteById(id);
    }
}
