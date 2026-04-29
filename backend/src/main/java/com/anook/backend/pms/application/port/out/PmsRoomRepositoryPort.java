package com.anook.backend.pms.application.port.out;

import com.anook.backend.pms.domain.model.PmsRoom;

import java.util.List;

/**
 * PMS 객실 영속성 포트 — 도메인 모델만 반환
 */
public interface PmsRoomRepositoryPort {

    /** 전체 객실 + 투숙 상태 (LEFT JOIN pms_guest) */
    List<PmsRoom> findAllWithOccupancy();

    /** 전체 객실 번호 목록 (동기화용) */
    List<String> findAllRoomNumbers();
}
