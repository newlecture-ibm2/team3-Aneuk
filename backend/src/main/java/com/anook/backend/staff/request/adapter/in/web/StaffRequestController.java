package com.anook.backend.staff.request.adapter.in.web;

import com.anook.backend.staff.request.application.dto.response.GetStaffRequestsResult;
import com.anook.backend.staff.request.application.port.in.GetStaffRequestsUseCase;
import com.anook.backend.staff.request.application.port.in.ProcessStaffRequestUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [직원 모듈] 요청 관리 API
 * 
 * 규칙: 직원 관련 모든 API는 /staff/requests/** 경로를 사용하며, 
 * staff 모듈 내부에 위치해야 함.
 */
@RestController
@RequestMapping("/staff/requests")
@RequiredArgsConstructor
public class StaffRequestController {

    private final GetStaffRequestsUseCase getStaffRequestsUseCase;
    private final ProcessStaffRequestUseCase processStaffRequestUseCase;

    /**
     * 작업 목록 조회 (필터 포함)
     */
    @GetMapping
    public ResponseEntity<List<GetStaffRequestsResult>> getRequests(
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "ALL") String priority,
            @RequestParam(defaultValue = "ALL") String departmentId
    ) {
        List<GetStaffRequestsResult> results = getStaffRequestsUseCase.getRequests(status, priority, departmentId);
        return ResponseEntity.ok(results);
    }

    /**
     * 작업 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<GetStaffRequestsResult> getRequestDetail(@PathVariable Long id) {
        return ResponseEntity.ok(getStaffRequestsUseCase.getRequestDetail(id));
    }

    /**
     * 작업 수락
     */
    @PatchMapping("/{id}/accept")
    public ResponseEntity<Void> acceptRequest(@PathVariable Long id) {
        processStaffRequestUseCase.accept(id, "김하우스"); // 실제 구현 시 세션에서 이름 추출
        return ResponseEntity.ok().build();
    }

    /**
     * 작업 완료
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> completeRequest(@PathVariable Long id) {
        processStaffRequestUseCase.complete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 작업 거절/반려
     */
    @PatchMapping("/{id}/reject")
    public ResponseEntity<Void> rejectRequest(@PathVariable Long id) {
        processStaffRequestUseCase.reject(id);
        return ResponseEntity.ok().build();
    }
}
