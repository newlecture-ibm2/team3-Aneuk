package com.anook.backend.guest.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 투숙객 도메인 모델 (순수 POJO — JPA 의존 없음)
 */
public class Guest {

    private Long id;
    private Long roomId;
    private String accessCode; 
    private String guestName;
    private String language;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDate checkoutDate;

    // === 기본 생성자 ===
    protected Guest() {}

    // === 전체 필드 생성자 ===
    public Guest(Long id, Long roomId, String accessCode, String guestName, String language, String notes,
                 LocalDateTime createdAt, LocalDate checkoutDate) {
        this.id = id;
        this.roomId = roomId;
        this.accessCode = accessCode;
        this.guestName = guestName;
        this.language = language;
        this.notes = notes;
        this.createdAt = createdAt;
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
    public LocalDate getCheckoutDate() { return checkoutDate; }
}
