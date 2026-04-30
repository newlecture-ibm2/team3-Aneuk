package com.anook.backend.guest.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * PMS 투숙객 도메인 모델 (순수 POJO — JPA 의존 없음)
 *
 * 체크아웃 시 Hard Delete로 PII를 최소화합니다.
 * PMS 전용 — ANOOK에서는 이 모델을 사용하지 않습니다.
 */
public class Guest {

    private Long id;
    private String roomNumber;
    private String name;
    private String phone;
    private LocalDateTime checkinDate;
    private LocalDate checkoutDate;

    // === 기본 생성자 (프레임워크용) ===
    protected Guest() {}

    // === 전체 필드 생성자 (Entity → Domain 변환용) ===
    public Guest(Long id, String roomNumber, String name, String phone,
                 LocalDateTime checkinDate, LocalDate checkoutDate) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.name = name;
        this.phone = phone;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
    }

    // === 팩토리 메서드 (체크인 시 사용) ===
    public static Guest create(String roomNumber, String name, String phone, LocalDate checkoutDate) {
        Guest guest = new Guest();
        guest.roomNumber = roomNumber;
        guest.name = name;
        guest.phone = phone;
        guest.checkinDate = LocalDateTime.now();
        guest.checkoutDate = checkoutDate;
        return guest;
    }

    // === Getter ===
    public Long getId() { return id; }
    public String getRoomNumber() { return roomNumber; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public LocalDateTime getCheckinDate() { return checkinDate; }
    public LocalDate getCheckoutDate() { return checkoutDate; }
}
