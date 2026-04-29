package com.anook.backend.request.adapter.in.web;

import com.anook.backend.request.application.dto.response.GetStaffTasksResult;
import com.anook.backend.request.application.port.in.GetStaffTasksUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 직원용 작업 목록 조회 API
 *
 * ❌ /api 접두어 없음 (BFF가 제거)
 */
@RestController
@RequestMapping("/staff/requests")
@RequiredArgsConstructor
public class StaffRequestController {

    private final GetStaffTasksUseCase getStaffTasksUseCase;

    /**
     * 직원용 작업 목록 조회 — GET /staff/requests
     *
     * @param status       상태 필터 (ALL이면 전체, 기본값 ALL)
     * @param priority     우선순위 필터 (ALL이면 전체, 기본값 ALL)
     * @param departmentId 부서 필터 (ALL이면 전체, 기본값 ALL)
     */
    @GetMapping
    public ResponseEntity<List<GetStaffTasksResult>> getStaffTasks(
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "ALL") String priority,
            @RequestParam(defaultValue = "ALL") String departmentId
    ) {
        List<GetStaffTasksResult> results = getStaffTasksUseCase.getTasks(status, priority, departmentId);
        return ResponseEntity.ok(results);
    }
}
