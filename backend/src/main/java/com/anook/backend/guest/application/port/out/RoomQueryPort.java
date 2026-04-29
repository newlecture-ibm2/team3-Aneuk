package com.anook.backend.guest.application.port.out;

/**
 * Room 모듈 조회용 포트 — 체크인 시 객실 존재 여부 검증에 사용
 */
public interface RoomQueryPort {

    /**
     * 해당 ID의 객실이 존재하는지 확인
     */
    boolean existsById(Long roomId);

    /**
     * roomId로 객실 번호 조회
     */
    String findRoomNumberById(Long roomId);
}
