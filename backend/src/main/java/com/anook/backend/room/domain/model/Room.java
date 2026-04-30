package com.anook.backend.room.domain.model;

import java.util.Objects;

/**
 * 객실 도메인 모델 — 순수 POJO (JPA, Spring 의존 금지)
 *
 * ANOOK에서는 호실 번호(number)만 관리합니다.
 */
public class Room {

    private final String number;

    public Room(String number) {
        this.number = Objects.requireNonNull(number, "객실 번호는 필수입니다.");
    }

    public String getNumber() { return number; }
}
