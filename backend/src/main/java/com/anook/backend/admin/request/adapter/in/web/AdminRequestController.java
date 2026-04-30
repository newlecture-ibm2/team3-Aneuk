package com.anook.backend.admin.request.adapter.in.web;

import com.anook.backend.admin.request.application.dto.request.AssignRequestCommand;
import com.anook.backend.admin.request.application.dto.request.ChangeRequestPriorityCommand;
import com.anook.backend.admin.request.application.dto.response.AdminRequestDetailResult;
import com.anook.backend.admin.request.application.dto.response.AdminRequestListResult;
import com.anook.backend.admin.request.application.port.in.ManageAdminRequestUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 요청 관리 Controller
 *
 * 전체 요청 모니터링, 담당자 배정, 우선순위 변경, 취소 기능을 제공합니다.
 */
@RestController
@RequestMapping("/admin/requests")
@RequiredArgsConstructor
public class AdminRequestController {

    private final ManageAdminRequestUseCase manageAdminRequestUseCase;

    /**
     * 전체 요청 목록 조회 (필터링 + 정렬)
     *
     * GET /admin/requests?status=PENDING&dept=HK&priority=URGENT&sort=created_at_desc
     */
    @GetMapping
    public ResponseEntity<List<AdminRequestListResult>> getAllRequests(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false, defaultValue = "created_at_desc") String sort) {
        return ResponseEntity.ok(manageAdminRequestUseCase.getAllRequests(status, dept, priority, sort));
    }

    /**
     * 요청 상세 조회
     *
     * GET /admin/requests/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdminRequestDetailResult> getRequestDetail(@PathVariable Long id) {
        return ResponseEntity.ok(manageAdminRequestUseCase.getRequestDetail(id));
    }

    /**
     * 담당자 배정/재배정
     *
     * PATCH /admin/requests/{id}/assign
     */
    @PatchMapping("/{id}/assign")
    public ResponseEntity<Void> assignRequest(
            @PathVariable Long id,
            @Valid @RequestBody AssignRequestCommand command) {
        manageAdminRequestUseCase.assignRequest(id, command);
        return ResponseEntity.noContent().build();
    }

    /**
     * 우선순위 변경
     *
     * PATCH /admin/requests/{id}/priority
     */
    @PatchMapping("/{id}/priority")
    public ResponseEntity<Void> changeRequestPriority(
            @PathVariable Long id,
            @Valid @RequestBody ChangeRequestPriorityCommand command) {
        manageAdminRequestUseCase.changeRequestPriority(id, command);
        return ResponseEntity.noContent().build();
    }

    /**
     * 요청 취소
     *
     * PATCH /admin/requests/{id}/cancel
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelRequest(@PathVariable Long id) {
        manageAdminRequestUseCase.cancelRequest(id);
        return ResponseEntity.noContent().build();
    }
}
