package com.anook.backend.room.adapter.out.persistence;

import com.anook.backend.room.application.port.out.PmsRoomQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * PMS 객실 번호 읽기 어댑터
 *
 * pms_room 테이블에서 객실 번호만 조회 (JdbcTemplate 네이티브 쿼리).
 * JPA Entity 중복 방지를 위해 JdbcTemplate 사용.
 */
@Component
@RequiredArgsConstructor
public class PmsRoomQueryAdapter implements PmsRoomQueryPort {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<String> findAllRoomNumbers() {
        return jdbcTemplate.queryForList(
                "SELECT number FROM pms_room ORDER BY number", String.class
        );
    }
}
