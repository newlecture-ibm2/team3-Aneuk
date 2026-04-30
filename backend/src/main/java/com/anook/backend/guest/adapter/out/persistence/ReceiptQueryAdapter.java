package com.anook.backend.guest.adapter.out.persistence;

import com.anook.backend.guest.application.port.out.ReceiptQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ReceiptQueryPort 구현체 — JdbcTemplate으로 pms_receipt 테이블 조회
 *
 * Guest 모듈에서 pms_receipt를 읽기 전용으로 접근한다.
 * PMS 모듈의 JPA Entity를 import하지 않고 네이티브 쿼리 사용.
 */
@Component
@RequiredArgsConstructor
public class ReceiptQueryAdapter implements ReceiptQueryPort {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean hasUnpaidReceipts(String roomNo) {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM pms_receipt WHERE room_no = ? AND status = 'UNPAID'",
                Long.class,
                roomNo
        );
        return count != null && count > 0;
    }

    @Override
    public List<UnpaidReceiptInfo> findUnpaidByRoomNo(String roomNo) {
        return jdbcTemplate.query(
                """
                SELECT r.id, m.name AS menu_name, r.quantity, r.total_price
                FROM pms_receipt r
                JOIN pms_menu m ON r.menu_id = m.id
                WHERE r.room_no = ? AND r.status = 'UNPAID'
                ORDER BY r.created_at DESC
                """,
                (rs, rowNum) -> new UnpaidReceiptInfo(
                        rs.getLong("id"),
                        rs.getString("menu_name"),
                        rs.getInt("quantity"),
                        rs.getInt("total_price")
                ),
                roomNo
        );
    }
}
