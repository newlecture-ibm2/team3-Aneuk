package com.anook.backend.request.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Request JPA Repository — request 모듈 전용
 */
public interface RequestJpaRepository extends JpaRepository<RequestJpaEntity, Long> {

    /**
     * 방번호로 해당 객실의 모든 요청 조회
     */
    List<RequestJpaEntity> findByRoomNo(String roomNo);
}
