package com.anook.backend.request.adapter.out.persistence;

import com.anook.backend.request.application.port.out.RequestRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * RequestRepositoryPort 구현체 — Request 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class RequestPersistenceAdapter implements RequestRepositoryPort {

    private final RequestJpaRepository jpaRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<RequestStatusDto> findStatusById(Long id) {
        return jpaRepository.findById(id)
                .map(entity -> new RequestStatusDto(
                        entity.getId(),
                        entity.getStatus(),
                        entity.getDepartmentId(),
                        entity.getSummary()
                ));
    }

    @Override
    public void updateStatus(Long id, String status) {
        RequestJpaEntity entity = jpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + id));
        entity.updateStatus(status);
        jpaRepository.save(entity);
    }

    @Override
    public List<StaffTaskRow> findAllByFilters(String status, String priority, String departmentId) {
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

        if (status != null) {
            sql.append(" AND r.status = ?");
            params.add(status);
        }
        if (priority != null) {
            sql.append(" AND r.priority = ?");
            params.add(priority);
        }
        if (departmentId != null) {
            sql.append(" AND r.department_id = ?");
            params.add(departmentId);
        }

        sql.append(" ORDER BY r.created_at DESC");

        RowMapper<StaffTaskRow> rowMapper = (rs, rowNum) -> new StaffTaskRow(
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

        return jdbcTemplate.query(sql.toString(), rowMapper, params.toArray());
    }
}
