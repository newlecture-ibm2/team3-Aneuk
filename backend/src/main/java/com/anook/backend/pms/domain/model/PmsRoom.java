package com.anook.backend.pms.domain.model;

/**
 * PMS 객실 도메인 모델 (순수 POJO)
 *
 * 호텔 운영 시스템이 관리하는 객실 정보.
 * ANOOK은 이 데이터에 직접 접근하지 않으며, 동기화 시 number만 수신한다.
 */
public class PmsRoom {

    private final String number;
    private final String type;
    private final boolean occupied;
    private final String guestName;

    public PmsRoom(String number, String type, boolean occupied, String guestName) {
        this.number = number;
        this.type = type;
        this.occupied = occupied;
        this.guestName = guestName;
    }

    public String getNumber() { return number; }
    public String getType() { return type; }
    public boolean isOccupied() { return occupied; }
    public String getGuestName() { return guestName; }
}
