package com.anook.backend.request.application.port.out;

import java.util.Optional;

/**
 * Request 영속성 포트 — 정산 기능에 필요한 최소 인터페이스
 */
public interface RequestRepositoryPort {

    /**
     * ID로 요청의 현재 상태 조회
     */
    Optional<RequestStatusDto> findStatusById(Long id);

    /**
     * 요청 상태 변경
     */
    void updateStatus(Long id, String status);

    /**
     * 상태 조회 DTO (Port 레벨에서 사용하는 경량 DTO)
     */
    record RequestStatusDto(
            Long id,
            String status,
            String departmentId,
            String summary
    ) {}
}
