package com.anook.backend.guest.adapter.out.persistence;

import com.anook.backend.guest.application.port.out.RoomQueryPort;
import com.anook.backend.guest.domain.exception.RoomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * RoomQueryPort 구현체 — JdbcTemplate으로 Room 테이블 조회
 *
 * Room JPA Entity는 room 모듈이 소유하므로,
 * guest 모듈에서는 JdbcTemplate 읽기 전용 쿼리로 접근합니다.
 */
@Component
@RequiredArgsConstructor
public class RoomQueryAdapter implements RoomQueryPort {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean existsById(Long roomId) {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM room WHERE id = ?",
                Long.class, roomId);
        return count != null && count > 0;
    }

    @Override
    public String findRoomNumberById(Long roomId) {
        return jdbcTemplate.query(
                "SELECT number FROM room WHERE id = ?",
                rs -> {
                    if (rs.next()) return rs.getString("number");
                    throw new RoomNotFoundException("객실을 찾을 수 없습니다. roomId=" + roomId);
                },
                roomId
        );
    }
}
