package com.anook.backend.request.adapter.in.web;

import com.anook.backend.request.application.port.in.ChangeRequestStatusUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 직원 전용 요청 상태 변경 컨트롤러
 */
@RestController
@RequestMapping("/staff/requests")
@RequiredArgsConstructor
public class StaffRequestController {

    private final ChangeRequestStatusUseCase changeRequestStatusUseCase;

    @PatchMapping("/{id}/accept")
    public ResponseEntity<String> acceptRequest(@PathVariable Long id, @RequestBody StaffActionDto dto) {
        changeRequestStatusUseCase.acceptRequest(id, dto.staffId());
        return ResponseEntity.ok("요청 수락 및 담당자 배정 완료");
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<String> completeRequest(@PathVariable Long id, @RequestBody StaffActionDto dto) {
        changeRequestStatusUseCase.completeRequest(id, dto.staffId());
        return ResponseEntity.ok("요청 완료 처리 완료");
    }

    public record StaffActionDto(Long staffId) {}
}
