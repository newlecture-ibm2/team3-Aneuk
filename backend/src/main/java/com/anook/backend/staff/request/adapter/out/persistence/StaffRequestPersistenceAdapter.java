package com.anook.backend.staff.request.adapter.out.persistence;

import com.anook.backend.staff.request.application.dto.response.GetStaffRequestsResult;
import com.anook.backend.staff.request.application.port.out.StaffRequestRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * [직원 모듈] 요청 영속성 어댑터
 * 
 * 규칙: 타 모듈 테이블 조회 시 JdbcTemplate 네이티브 쿼리를 사용하며, JPA Entity를 참조하지 않음.
 */
@Component
@RequiredArgsConstructor
public class StaffRequestPersistenceAdapter implements StaffRequestRepositoryPort {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<GetStaffRequestsResult> rowMapper = (rs, rowNum) -> new GetStaffRequestsResult(
            rs.getLong("id"),
            rs.getString("status"),
            rs.getString("priority"),
            rs.getString("department_id"),
            rs.getString("summary"),
            rs.getString("raw_text"),
            rs.getString("room_number"),
            rs.getString("staff_name"),
            rs.getObject("confidence") != null ? rs.getFloat("confidence") : null,
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    @Override
    public List<GetStaffRequestsResult> findAllByFilters(String status, String priority, String departmentId) {
        StringBuilder sql = new StringBuilder("""
                SELECT r.id, r.status, r.priority, r.department_id,
                       r.summary, r.raw_text, rm.number AS room_number,
                       s.name AS staff_name, r.confidence, r.created_at
                FROM request r
                JOIN room rm ON r.room_id = rm.id
                LEFT JOIN staff s ON r.assigned_staff_id = s.id
                WHERE 1=1
                """);

        List<Object> params = new ArrayList<>();
        if (status != null) { sql.append(" AND r.status = ?"); params.add(status); }
        if (priority != null) { sql.append(" AND r.priority = ?"); params.add(priority); }
        if (departmentId != null) { sql.append(" AND r.department_id = ?"); params.add(departmentId); }

        sql.append(" ORDER BY r.created_at DESC");
        return jdbcTemplate.query(sql.toString(), rowMapper, params.toArray());
    }

    @Override
    public Optional<GetStaffRequestsResult> findById(Long id) {
        String sql = """
                SELECT r.id, r.status, r.priority, r.department_id,
                       r.summary, r.raw_text, rm.number AS room_number,
                       s.name AS staff_name, r.confidence, r.created_at
                FROM request r
                JOIN room rm ON r.room_id = rm.id
                LEFT JOIN staff s ON r.assigned_staff_id = s.id
                WHERE r.id = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }
}
