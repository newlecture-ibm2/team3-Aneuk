package com.anook.backend.admin.staff.application.service;

import com.anook.backend.admin.staff.application.dto.request.CreateStaffCommand;
import com.anook.backend.admin.staff.application.dto.request.UpdateStaffCommand;
import com.anook.backend.admin.staff.application.dto.response.GetStaffResult;
import com.anook.backend.admin.staff.application.port.in.ManageStaffUseCase;
import com.anook.backend.admin.staff.application.port.out.DepartmentQueryPort;
import com.anook.backend.admin.staff.application.port.out.RoleQueryPort;
import com.anook.backend.admin.staff.application.port.out.StaffRepositoryPort;
import com.anook.backend.admin.staff.domain.model.Staff;
import com.anook.backend.global.exception.BusinessException;
import com.anook.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 직원 관리 서비스 — ManageStaffUseCase 구현체
 *
 * Port(Out)만 의존, JPA Repository 직접 import 금지
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManageStaffService implements ManageStaffUseCase {

    private final StaffRepositoryPort staffRepositoryPort;
    private final DepartmentQueryPort departmentQueryPort;
    private final RoleQueryPort roleQueryPort;

    @Override
    @Transactional
    public GetStaffResult create(CreateStaffCommand command) {
        validateDepartmentExists(command.departmentId());
        validateRoleExists(command.roleId());

        String pin = generatePin();
        Staff staff = new Staff(null, command.name(), pin,
                command.roleId(), command.departmentId());
        Staff saved = staffRepositoryPort.save(staff);
        return GetStaffResult.from(saved);
    }

    @Override
    public List<GetStaffResult> getAll() {
        return staffRepositoryPort.findAll().stream()
                .map(GetStaffResult::from)
                .toList();
    }

    @Override
    public GetStaffResult getById(Long id) {
        Staff staff = findStaffOrThrow(id);
        return GetStaffResult.from(staff);
    }

    @Override
    public List<GetStaffResult> getByDepartmentId(String departmentId) {
        return staffRepositoryPort.findByDepartmentId(departmentId).stream()
                .map(GetStaffResult::from)
                .toList();
    }

    @Override
    @Transactional
    public GetStaffResult update(Long id, UpdateStaffCommand command) {
        Staff existing = findStaffOrThrow(id);

        // 부서 변경 시 존재 여부 확인
        String departmentId = command.departmentId() != null
                ? command.departmentId() : existing.getDepartmentId();
        if (command.departmentId() != null) {
            validateDepartmentExists(departmentId);
        }
        // 역할 변경 시 존재 여부 확인
        if (command.roleId() != null) {
            validateRoleExists(command.roleId());
        }

        Staff updated = new Staff(
                id,
                command.name() != null ? command.name() : existing.getName(),
                existing.getPin(),
                command.roleId() != null ? command.roleId() : existing.getRoleId(),
                departmentId
        );

        Staff saved = staffRepositoryPort.save(updated);
        return GetStaffResult.from(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!staffRepositoryPort.existsById(id)) {
            throw new BusinessException(ErrorCode.STAFF_NOT_FOUND);
        }
        staffRepositoryPort.deleteById(id);
    }

    private Staff findStaffOrThrow(Long id) {
        return staffRepositoryPort.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.STAFF_NOT_FOUND));
    }

    private void validateDepartmentExists(String departmentId) {
        if (!departmentQueryPort.existsById(departmentId)) {
            throw new BusinessException(ErrorCode.DEPARTMENT_NOT_FOUND);
        }
    }

    private void validateRoleExists(Long roleId) {
        if (!roleQueryPort.existsById(roleId)) {
            throw new BusinessException(ErrorCode.ROLE_NOT_FOUND);
        }
    }

    /**
     * 6자리 숫자 PIN 자동 생성
     */
    private String generatePin() {
        int pin = ThreadLocalRandom.current().nextInt(100_000, 1_000_000);
        return String.valueOf(pin);
    }
}
