package com.anook.backend.admin.request.application.service;

import com.anook.backend.admin.department.application.dto.response.DepartmentInfo;
import com.anook.backend.admin.department.application.port.in.ListDepartmentsUseCase;
import com.anook.backend.admin.request.application.dto.request.AssignRequestCommand;
import com.anook.backend.admin.request.application.dto.request.ChangeRequestPriorityCommand;
import com.anook.backend.admin.request.application.dto.request.CreateAdminRequestCommand;
import com.anook.backend.admin.request.application.dto.response.AdminRequestDetailResult;
import com.anook.backend.admin.request.application.dto.response.AdminRequestListResult;
import com.anook.backend.admin.request.application.port.in.ManageAdminRequestUseCase;
import com.anook.backend.admin.request.application.port.out.AdminRequestQueryPort;
import com.anook.backend.admin.request.domain.model.AdminRequest;
import com.anook.backend.admin.staff.application.dto.response.GetStaffResult;
import com.anook.backend.admin.staff.application.port.in.ManageStaffUseCase;
import com.anook.backend.global.exception.BusinessException;
import com.anook.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 관리자 요청 관리 Service
 *
 * 부서명/직원명은 다른 모듈의 UseCase(Port In)를 통해 조회합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManageAdminRequestService implements ManageAdminRequestUseCase {

    private final AdminRequestQueryPort adminRequestQueryPort;
    private final ListDepartmentsUseCase listDepartmentsUseCase;
    private final ManageStaffUseCase manageStaffUseCase;

    @Override
    public List<AdminRequestListResult> getAllRequests(String status, String departmentId, String priority, String sort) {
        List<AdminRequest> requests = adminRequestQueryPort.findAll(status, departmentId, priority, sort);

        // 부서명/직원명 조회용 Map 구성
        Map<String, String> deptNameMap = buildDeptNameMap();
        Map<Long, String> staffNameMap = buildStaffNameMap();

        return requests.stream()
                .map(r -> toListResult(r, deptNameMap, staffNameMap))
                .toList();
    }

    @Override
    public AdminRequestDetailResult getRequestDetail(Long id) {
        AdminRequest request = adminRequestQueryPort.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.REQUEST_NOT_FOUND));

        Map<String, String> deptNameMap = buildDeptNameMap();
        Map<Long, String> staffNameMap = buildStaffNameMap();

        return toDetailResult(request, deptNameMap, staffNameMap);
    }

    @Override
    @Transactional
    public void assignRequest(Long id, AssignRequestCommand command) {
        AdminRequest request = adminRequestQueryPort.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.REQUEST_NOT_FOUND));

        if (!request.isAssignable()) {
            throw new IllegalStateException("현재 상태에서는 담당자를 배정할 수 없습니다: " + request.getStatus());
        }

        adminRequestQueryPort.assignStaff(id, command.staffId());
    }

    @Override
    @Transactional
    public void changeRequestPriority(Long id, ChangeRequestPriorityCommand command) {
        adminRequestQueryPort.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.REQUEST_NOT_FOUND));

        adminRequestQueryPort.updatePriority(id, command.priority().toUpperCase());
    }

    @Override
    @Transactional
    public void cancelRequest(Long id) {
        AdminRequest request = adminRequestQueryPort.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.REQUEST_NOT_FOUND));

        if (!request.isCancellable()) {
            throw new IllegalStateException("이미 취소되었거나 정산 완료된 요청입니다: " + request.getStatus());
        }

        adminRequestQueryPort.cancel(id);
    }

    @Override
    public List<AdminRequestListResult> getEscalations() {
        List<AdminRequest> overdue = adminRequestQueryPort.findOverdue();

        Map<String, String> deptNameMap = buildDeptNameMap();
        Map<Long, String> staffNameMap = buildStaffNameMap();

        return overdue.stream()
                .map(r -> toListResult(r, deptNameMap, staffNameMap))
                .toList();
    }

    @Override
    @Transactional
    public void escalateRequest(Long id) {
        adminRequestQueryPort.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.REQUEST_NOT_FOUND));

        adminRequestQueryPort.escalate(id);
    }

    @Override
    @Transactional
    public AdminRequestDetailResult createRequest(CreateAdminRequestCommand command) {
        AdminRequest saved = adminRequestQueryPort.save(
                command.departmentId().toUpperCase(),
                command.roomNo(),
                command.summary(),
                command.rawText(),
                command.priority(),
                command.assignedStaffId()
        );

        Map<String, String> deptNameMap = buildDeptNameMap();
        Map<Long, String> staffNameMap = buildStaffNameMap();

        return toDetailResult(saved, deptNameMap, staffNameMap);
    }

    // === 다른 모듈 데이터 조회 ===

    private Map<String, String> buildDeptNameMap() {
        return listDepartmentsUseCase.getAll().stream()
                .collect(Collectors.toMap(DepartmentInfo::id, DepartmentInfo::name));
    }

    private Map<Long, String> buildStaffNameMap() {
        return manageStaffUseCase.getAll().stream()
                .collect(Collectors.toMap(GetStaffResult::id, GetStaffResult::name));
    }

    // === Domain → DTO 변환 (이름 매핑 포함) ===

    private AdminRequestListResult toListResult(AdminRequest r, Map<String, String> deptMap, Map<Long, String> staffMap) {
        return new AdminRequestListResult(
                r.getId(), r.getStatus(), r.getPriority(),
                r.getDepartmentId(),
                deptMap.getOrDefault(r.getDepartmentId(), r.getDepartmentId()),
                r.getSummary(), r.getRoomNo(),
                r.getAssignedStaffId(),
                r.getAssignedStaffId() != null ? staffMap.get(r.getAssignedStaffId()) : null,
                r.getCreatedAt(), r.getUpdatedAt()
        );
    }

    private AdminRequestDetailResult toDetailResult(AdminRequest r, Map<String, String> deptMap, Map<Long, String> staffMap) {
        return new AdminRequestDetailResult(
                r.getId(), r.getStatus(), r.getPriority(),
                r.getDepartmentId(),
                deptMap.getOrDefault(r.getDepartmentId(), r.getDepartmentId()),
                r.getEntities(), r.getRawText(), r.getSummary(),
                r.getConfidence(), r.getRoomNo(),
                r.getAssignedStaffId(),
                r.getAssignedStaffId() != null ? staffMap.get(r.getAssignedStaffId()) : null,
                r.getVersion(), r.getCreatedAt(), r.getUpdatedAt()
        );
    }
}
