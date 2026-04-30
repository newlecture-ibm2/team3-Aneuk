package com.anook.backend.message.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 메시지 JPA Repository
 *
 * ⚠️ Service에서 직접 import 금지
 * ⚠️ MessagePersistenceAdapter 내부에서만 사용
 */
public interface MessageJpaRepository extends JpaRepository<MessageJpaEntity, Long> {

    /**
     * 특정 객실의 메시지를 시간순으로 조회
     */
    List<MessageJpaEntity> findByRoomIdOrderByCreatedAtAsc(Long roomId);

    /**
     * 메시지가 존재하는 객실 ID 목록 (관리자 채팅 히스토리용)
     */
    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT m.roomId FROM MessageJpaEntity m ORDER BY m.roomId")
    List<Long> findDistinctRoomIds();
}
