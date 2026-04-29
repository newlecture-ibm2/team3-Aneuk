package com.anook.backend.request.application.service;

import com.anook.backend.request.application.dto.response.RequestWebSocketPayload;
import com.anook.backend.request.application.port.in.ChangeRequestStatusUseCase;
import com.anook.backend.request.application.port.out.DispatchPort;
import com.anook.backend.request.application.port.out.RequestRepositoryPort;
import com.anook.backend.request.domain.model.Request;
import com.anook.backend.request.domain.model.RequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeRequestStatusService implements ChangeRequestStatusUseCase {

    private final RequestRepositoryPort requestRepositoryPort;
    private final DispatchPort dispatchPort;

    @Override
    @Transactional
    public void acceptRequest(Long requestId, Long staffId) {
        Request request = requestRepositoryPort.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("요청을 찾을 수 없습니다. id=" + requestId));

        // 담당자 배정 (PENDING -> ASSIGNED)
        request.assignStaff(staffId);
        
        // 작업 시작 (ASSIGNED -> IN_PROGRESS)
        request.changeStatus(RequestStatus.IN_PROGRESS);

        requestRepositoryPort.save(request);
        log.info("요청 수락 완료: requestId={}, staffId={}", requestId, staffId);

        // [RQ-5] WebSocket 알림 발송 (고객 & 부서)
        RequestWebSocketPayload payload = RequestWebSocketPayload.statusChanged(
                request.getId(),
                request.getStatus().name(),
                request.getDomainCode() != null ? request.getDomainCode().name() : "UNKNOWN",
                request.getSummary(),
                request.getRoomNo()
        );
        dispatchPort.dispatchToRoom(request.getRoomNo(), payload);
        if (request.getDomainCode() != null) {
            dispatchPort.dispatchToDepartment(request.getDomainCode().name(), payload);
        }
    }

    @Override
    @Transactional
    public void completeRequest(Long requestId, Long staffId) {
        Request request = requestRepositoryPort.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("요청을 찾을 수 없습니다. id=" + requestId));

        // 작업 완료 (IN_PROGRESS/ASSIGNED -> COMPLETED)
        request.changeStatus(RequestStatus.COMPLETED);

        requestRepositoryPort.save(request);
        log.info("요청 처리 완료: requestId={}, staffId={}", requestId, staffId);

        // [RQ-5] WebSocket 알림 발송 (고객 & 부서)
        RequestWebSocketPayload payload = RequestWebSocketPayload.statusChanged(
                request.getId(),
                request.getStatus().name(),
                request.getDomainCode() != null ? request.getDomainCode().name() : "UNKNOWN",
                request.getSummary(),
                request.getRoomNo()
        );
        dispatchPort.dispatchToRoom(request.getRoomNo(), payload);
        if (request.getDomainCode() != null) {
            dispatchPort.dispatchToDepartment(request.getDomainCode().name(), payload);
        }
    }
}
