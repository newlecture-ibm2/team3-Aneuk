package com.anook.backend.guest.adapter.out.persistence;

import com.anook.backend.guest.application.port.out.RoomQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * RoomQueryPort 구현체 — JdbcTemplate으로 pms_room 테이블 조회
 */
@Component
@RequiredArgsConstructor
public class RoomQueryAdapter implements RoomQueryPort {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean existsByNumber(String roomNumber) {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM pms_room WHERE number = ?",
                Long.class, roomNumber);
        return count != null && count > 0;
    }
}
