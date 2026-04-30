package com.anook.backend.admin.message.adapter.out.persistence;

import com.anook.backend.admin.message.adapter.out.persistence.entity.AdminMessageJpaEntity;
import com.anook.backend.admin.message.application.port.out.AdminMessageQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 관리자 메시지 PersistenceAdapter
 *
 * AdminMessageQueryPort 구현체.
 * JPA Entity로 message 테이블 조회 + JdbcTemplate으로 room 테이블 읽기 전용 조회.
 * message 모듈의 코드를 import하지 않음 (독립성 유지).
 */
@Component
@RequiredArgsConstructor
public class AdminMessagePersistenceAdapter implements AdminMessageQueryPort {

    private final AdminMessageJpaRepository jpaRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> findRoomsWithMessages() {
        List<Long> roomIds = jpaRepository.findDistinctRoomIds();

        if (roomIds.isEmpty()) {
            return List.of();
        }

        // roomId → roomNo 매핑 (room 테이블 읽기 전용)
        String inClause = roomIds.stream()
                .map(String::valueOf)
                .reduce((a, b) -> a + "," + b)
                .orElse("");

        return jdbcTemplate.queryForList(
                "SELECT id AS \"roomId\", number AS \"roomNo\" FROM room WHERE id IN (" + inClause + ") ORDER BY number"
        );
    }

    @Override
    public List<Map<String, Object>> findMessagesByRoomNo(String roomNo) {
        // roomNo → roomId 변환
        List<Long> roomIdResult = jdbcTemplate.queryForList(
                "SELECT id FROM room WHERE number = ?",
                Long.class, roomNo
        );

        if (roomIdResult.isEmpty()) {
            return List.of();
        }

        Long roomId = roomIdResult.get(0);
        List<AdminMessageJpaEntity> entities = jpaRepository.findByRoomIdOrderByCreatedAtAsc(roomId);

        return entities.stream().map(e -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", e.getId());
            map.put("senderType", e.getSenderType());
            map.put("content", e.getContent());
            map.put("translatedContent", e.getTranslatedContent());
            map.put("createdAt", e.getCreatedAt());
            return map;
        }).toList();
    }
}
