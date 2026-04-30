package com.anook.backend.guest.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PMS 투숙객 도메인 모델 (순수 POJO — JPA 의존 없음)
 *
 * 체크아웃 시 Hard Delete로 PII를 최소화합니다.
 * PMS 전용 — ANOOK에서는 이 모델을 사용하지 않습니다.
 */
public class Guest {

    private Long id;
    private Long roomId;
    private String accessCode; 
    private String guestName;
    private String language;
    private String notes;
    private LocalDateTime createdAt;
    private String roomNumber;
    private String name;
    private String phone;
    private LocalDateTime checkinDate;
    private LocalDate checkoutDate;

    // === 기본 생성자 ===
    protected Guest() {}

    // === 전체 필드 생성자 ===
    public Guest(Long id, Long roomId, String accessCode, String guestName, String language, String notes,
                 LocalDateTime createdAt, String roomNumber, String name, String phone,
                 LocalDateTime checkinDate, LocalDate checkoutDate) {
        this.id = id;
        this.roomId = roomId;
        this.accessCode = accessCode;
        this.guestName = guestName;
        this.language = language;
        this.notes = notes;
        this.createdAt = createdAt;
        this.roomNumber = roomNumber;
        this.name = name;
        this.phone = phone;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
    }

    /**
     * 팩토리 메서드 (체크인 시 사용)
     * 보안을 위한 랜덤 접속 코드(UUID)를 자동으로 생성합니다.
     */
    public static Guest create(Long roomId, String guestName, String language, LocalDate checkoutDate) {
        return new Guest(
                null,
                roomId,
                UUID.randomUUID().toString(), // 랜덤 코드 자동 생성
                guestName,
                language,
                null,
                LocalDateTime.now(),
                checkoutDate
        );
    }

    // === Getters ===
    public Long getId() { return id; }
    public Long getRoomId() { return roomId; }
    public String getAccessCode() { return accessCode; }
    public String getGuestName() { return guestName; }
    public String getLanguage() { return language; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    // === 전체 필드 생성자 (Entity → Domain 변환용) ===
    public Guest(Long id, String roomNumber, String name, String phone,
                 LocalDateTime checkinDate, LocalDate checkoutDate) {
        this.id = id;
        
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
