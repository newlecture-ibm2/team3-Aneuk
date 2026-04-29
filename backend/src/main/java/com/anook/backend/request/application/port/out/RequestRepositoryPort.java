package com.anook.backend.request.application.port.out;

import com.anook.backend.request.domain.model.Request;

import java.util.List;
import java.util.Optional;

/**
 * Request 영속성 포트 — 도메인 모델만 반환
 */
public interface RequestRepositoryPort {

    // === 기존 (정산 기능용) ===

    /**
     * ID로 요청의 현재 상태 조회 (경량 DTO)
     */
    Optional<RequestStatusDto> findStatusById(Long id);

    /**
     * 요청 상태 변경
     */
    void updateStatus(Long id, String status);

    // === 신규 (RQ-1 추가) ===

    /**
     * Request 도메인 모델 저장
     */
    Request save(Request request);

    /**
     * 방번호로 해당 객실의 모든 요청 조회
     */
    List<Request> findByRoomNo(String roomNo);

    /**
     * ID로 Request 도메인 모델 조회
     */
    Optional<Request> findById(Long id);

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
