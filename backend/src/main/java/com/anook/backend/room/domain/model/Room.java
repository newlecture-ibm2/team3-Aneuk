package com.anook.backend.room.domain.model;

import java.util.Objects;

/**
 * 객실 도메인 모델 — 순수 POJO (JPA, Spring 의존 금지)
 */
public class Room {

    private final Long id;
    private final String number;
    private final Integer floor;
    private final String typeId;

    public Room(Long id, String number, Integer floor, String typeId) {
        this.id = id;
        this.number = Objects.requireNonNull(number, "객실 번호는 필수입니다.");
        this.floor = Objects.requireNonNull(floor, "층수는 필수입니다.");
        this.typeId = Objects.requireNonNull(typeId, "객실 타입은 필수입니다.");
    }

    public Long getId() { return id; }
    public String getNumber() { return number; }
    public Integer getFloor() { return floor; }
    public String getTypeId() { return typeId; }
}
