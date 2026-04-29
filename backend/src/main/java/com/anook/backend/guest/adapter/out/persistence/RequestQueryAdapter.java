package com.anook.backend.guest.adapter.out.persistence;

import com.anook.backend.guest.application.port.out.RequestQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * RequestQueryPort 구현체 — JdbcTemplate으로 Request 테이블 조회
 *
 * request 테이블의 room_number 컬럼을 기반으로 조회합니다.
 */
@Component
@RequiredArgsConstructor
public class RequestQueryAdapter implements RequestQueryPort {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean existsByRoomNumberAndDepartmentIdAndStatusNotIn(
            String roomNumber, String departmentId, List<String> excludeStatuses) {

        String placeholders = String.join(",", Collections.nCopies(excludeStatuses.size(), "?"));

        String sql = String.format("""
                SELECT COUNT(*) FROM request
                WHERE room_number = ?
                  AND department_id = ?
                  AND status NOT IN (%s)
                """, placeholders);

        Object[] params = new Object[2 + excludeStatuses.size()];
        params[0] = roomNumber;
        params[1] = departmentId;
        for (int i = 0; i < excludeStatuses.size(); i++) {
            params[2 + i] = excludeStatuses.get(i);
        }

        Long count = jdbcTemplate.queryForObject(sql, Long.class, params);
        return count != null && count > 0;
    }
}
