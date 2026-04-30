package com.anook.backend.message.adapter.out.persistence;

import com.anook.backend.message.application.port.out.MessageRepositoryPort;
import com.anook.backend.message.domain.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 메시지 영속성 어댑터
 *
 * MessageRepositoryPort 구현체
 * Domain ↔ JPA Entity 변환을 이 클래스 안에서만 수행
 *
 * ✅ Port(Out)에는 도메인 모델(Message)만 반환
 * ❌ JPA Entity를 Port 밖으로 노출하지 않음
 *
 * roomNo → roomId 변환은 JdbcTemplate 네이티브 쿼리 사용 (다른 모듈 JPA Entity 중복 방지)
 */
@Component
@RequiredArgsConstructor
public class MessagePersistenceAdapter implements MessageRepositoryPort {

    private final MessageJpaRepository jpaRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Message save(Message message) {
        MessageJpaEntity entity = MessageJpaEntity.fromDomain(message);
        MessageJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public List<Message> findByRoomIdOrderByCreatedAt(Long roomId) {
        return jpaRepository.findByRoomIdOrderByCreatedAtAsc(roomId)
                .stream()
                .map(MessageJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Long> findRoomIdByRoomNo(String roomNo) {
        List<Long> result = jdbcTemplate.queryForList(
                "SELECT id FROM room WHERE number = ?",
                Long.class,
                roomNo
        );
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<Long> findDistinctRoomIds() {
        return jpaRepository.findDistinctRoomIds();
    }
}
