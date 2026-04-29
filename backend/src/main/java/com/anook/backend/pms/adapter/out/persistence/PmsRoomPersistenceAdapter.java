package com.anook.backend.pms.adapter.out.persistence;

import com.anook.backend.pms.application.port.out.PmsRoomRepositoryPort;
import com.anook.backend.pms.domain.model.PmsRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * PMS 객실 Persistence Adapter
 *
 * pms_room 테이블 조회 전용 (JdbcTemplate 사용).
 * JPA Entity를 만들지 않고 네이티브 쿼리로 읽기 전용 접근.
 */
@Component
@RequiredArgsConstructor
public class PmsRoomPersistenceAdapter implements PmsRoomRepositoryPort {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<PmsRoom> findAllWithOccupancy() {
        String sql = """
                SELECT r.number, r.type, g.name AS guest_name
                FROM pms_room r
                LEFT JOIN pms_guest g ON r.number = g.room_number
                ORDER BY r.number
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new PmsRoom(
                rs.getString("number"),
                rs.getString("type"),
                rs.getString("guest_name") != null,
                rs.getString("guest_name")
        ));
    }

    @Override
    public List<String> findAllRoomNumbers() {
        return jdbcTemplate.queryForList(
                "SELECT number FROM pms_room ORDER BY number", String.class
        );
    }
}
