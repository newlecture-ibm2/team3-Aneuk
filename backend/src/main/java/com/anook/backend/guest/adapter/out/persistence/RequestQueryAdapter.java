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
 * Guest 모듈에서 Request 테이블에 대한 JPA Entity를 가지지 않고,
 * JdbcTemplate으로 읽기 전용 조회만 수행합니다.
 * (Request JPA Entity는 request 모듈에서 관리)
 */
@Component
@RequiredArgsConstructor
public class RequestQueryAdapter implements RequestQueryPort {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean existsByRoomIdAndDepartmentIdAndStatusNotIn(
            Long roomId, String departmentId, List<String> excludeStatuses) {

        // IN 절 플레이스홀더 동적 생성
        String placeholders = String.join(",", Collections.nCopies(excludeStatuses.size(), "?"));

        String sql = String.format("""
                SELECT COUNT(*) FROM request
                WHERE room_id = ?
                  AND department_id = ?
                  AND status NOT IN (%s)
                """, placeholders);

        // 파라미터 배열 생성: [roomId, departmentId, status1, status2, ...]
        Object[] params = new Object[2 + excludeStatuses.size()];
        params[0] = roomId;
        params[1] = departmentId;
        for (int i = 0; i < excludeStatuses.size(); i++) {
            params[2 + i] = excludeStatuses.get(i);
        }

        Long count = jdbcTemplate.queryForObject(sql, Long.class, params);
        return count != null && count > 0;
    }
}
