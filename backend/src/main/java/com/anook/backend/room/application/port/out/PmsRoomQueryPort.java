package com.anook.backend.room.application.port.out;

import java.util.List;

/**
 * PMS 객실 번호 읽기 전용 포트 (모듈 간 읽기 접근)
 *
 * room 모듈이 pms_room 테이블에서 객실 번호만 읽어오기 위한 포트.
 * JdbcTemplate 네이티브 쿼리 사용 (다른 모듈의 JPA Entity 참조 방지).
 */
public interface PmsRoomQueryPort {

    List<String> findAllRoomNumbers();
}
