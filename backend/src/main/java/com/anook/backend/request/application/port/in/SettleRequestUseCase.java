package com.anook.backend.request.application.port.in;

import com.anook.backend.request.application.dto.response.SettleRequestResult;

/**
 * F&B 요청 정산 유스케이스
 */
public interface SettleRequestUseCase {
    SettleRequestResult settle(Long taskId);
}
