package com.anook.backend.guest.application.port.out;

/**
 * PMS Room 조회용 포트 — pms_room 테이블 참조
 */
public interface RoomQueryPort {

    /**
     * 해당 번호의 PMS 객실이 존재하는지 확인
     */
    boolean existsByNumber(String roomNumber);
}
