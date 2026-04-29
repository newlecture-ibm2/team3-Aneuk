package com.anook.backend.room.domain.model;

import java.util.Objects;

/**
 * 객실 도메인 모델 — 순수 POJO (JPA, Spring 의존 금지)
 */
public class Room {

    private final Long id;
    private final String number;
    private final Long typeId;

    public Room(Long id, String number, Long typeId) {
        this.id = id;
        this.number = Objects.requireNonNull(number, "객실 번호는 필수입니다.");
        this.typeId = Objects.requireNonNull(typeId, "객실 타입은 필수입니다.");
    }

    // === 팩토리 메서드 (객실 등록 시 사용) ===
    public static Room create(String number, Long typeId) {
        return new Room(null, number, typeId);
    }

    public Long getId() { return id; }
    public String getNumber() { return number; }
    public Long getTypeId() { return typeId; }
}
