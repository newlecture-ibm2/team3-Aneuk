package com.anook.backend.request.adapter.in.web;

import com.anook.backend.request.application.dto.response.SettleRequestResult;
import com.anook.backend.request.application.port.in.SettleRequestUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 정산 API — 프론트 직원이 F&B 결제 완료 후 호출
 *
 * ❌ /api 접두어 없음 (BFF가 제거)
 */
@RestController
@RequestMapping("/admin/tasks")
@RequiredArgsConstructor
public class AdminTaskSettleController {

    private final SettleRequestUseCase settleRequestUseCase;

    /**
     * F&B 정산 처리 — PATCH /admin/tasks/{taskId}/settle
     *
     * COMPLETED 상태의 FB 요청을 SETTLED로 변경
     */
    @PatchMapping("/{taskId}/settle")
    public ResponseEntity<SettleRequestResult> settle(@PathVariable Long taskId) {
        SettleRequestResult result = settleRequestUseCase.settle(taskId);
        return ResponseEntity.ok(result);
    }
}
