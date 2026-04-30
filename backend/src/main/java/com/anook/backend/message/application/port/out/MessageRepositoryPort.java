package com.anook.backend.message.application.port.out;

import com.anook.backend.message.domain.model.Message;

import java.util.List;
import java.util.Optional;

/**
 * 메시지 영속성 포트 (Out)
 *
 * Service → 이 인터페이스 → PersistenceAdapter(구현체)
 *
 * ❌ JPA Entity를 반환하지 않는다
 * ✅ 도메인 모델(Message)만 반환한다
 */
public interface MessageRepositoryPort {

    /**
     * 메시지 저장
     */
    Message save(Message message);

    /**
     * 특정 객실의 대화 내역 조회 (시간순 정렬)
     */
    List<Message> findByRoomIdOrderByCreatedAt(Long roomId);

    /**
     * 객실 번호(String)로 객실 ID(Long) 조회
     * ⚠️ 다른 모듈(room) 테이블을 읽기 전용으로 조회 (JdbcTemplate 네이티브 쿼리)
     */
    Optional<Long> findRoomIdByRoomNo(String roomNo);
}
