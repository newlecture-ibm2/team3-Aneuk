package com.anook.backend.request.application.service;

import com.anook.backend.request.application.dto.response.SettleRequestResult;
import com.anook.backend.request.application.port.in.SettleRequestUseCase;
import com.anook.backend.request.application.port.out.RequestRepositoryPort;
import com.anook.backend.request.application.port.out.RequestRepositoryPort.RequestStatusDto;
import com.anook.backend.global.exception.BusinessException;
import com.anook.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * F&B 요청 정산 서비스
 *
 * 정산 가능 조건:
 * - 해당 요청이 존재해야 함
 * - department_id가 'FB'여야 함
 * - status가 'COMPLETED'여야 함 (서비스 완료 후에만 정산 가능)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SettleRequestService implements SettleRequestUseCase {

    private static final String SETTLED = "SETTLED";
    private static final String COMPLETED = "COMPLETED";
    private static final String FB_DEPARTMENT = "FB";

    private final RequestRepositoryPort requestRepository;

    @Override
    public SettleRequestResult settle(Long taskId) {
        // 1) 요청 존재 확인
        RequestStatusDto request = requestRepository.findStatusById(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REQUEST_NOT_FOUND, "taskId=" + taskId));

        // 2) FB 부서 확인
        if (!FB_DEPARTMENT.equals(request.departmentId())) {
            throw new BusinessException(ErrorCode.INVALID_SETTLEMENT,
                    "현재 부서: " + request.departmentId());
        }

        // 3) COMPLETED 상태 확인
        if (!COMPLETED.equals(request.status())) {
            throw new BusinessException(ErrorCode.INVALID_SETTLEMENT,
                    "현재 상태: " + request.status());
        }

        // 4) 상태 변경 → SETTLED
        requestRepository.updateStatus(taskId, SETTLED);

        return new SettleRequestResult(taskId, SETTLED);
    }
}
