package com.anook.backend.guest.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 투숙객 도메인 모델 (순수 POJO — JPA 의존 없음)
 *
 * guest_name은 CRUD 표시용으로만 사용하며,
 * 체크아웃 시 Hard Delete로 PII를 최소화합니다.
 */
public class Guest {

    private Long id;
    private Long roomId;
    private String guestName;
    private String language;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDate checkoutDate;

    // === 기본 생성자 (프레임워크용) ===
    protected Guest() {}

    // === 전체 필드 생성자 (Entity → Domain 변환용) ===
    public Guest(Long id, Long roomId, String guestName, String language, String notes,
                 LocalDateTime createdAt, LocalDate checkoutDate) {
        this.id = id;
        this.roomId = roomId;
        this.guestName = guestName;
        this.language = language;
        this.notes = notes;
        this.createdAt = createdAt;
        this.checkoutDate = checkoutDate;
    }

    // === 팩토리 메서드 (체크인 시 사용) ===
    public static Guest create(Long roomId, String guestName, String language, LocalDate checkoutDate) {
        Guest guest = new Guest();
        guest.roomId = roomId;
        guest.guestName = guestName;
        guest.language = (language != null) ? language : "ko";
        guest.createdAt = LocalDateTime.now();
        guest.checkoutDate = checkoutDate;
        return guest;
    }

    // === 행위 메서드 (빈혈 도메인 방지) ===
    public void updateLanguage(String language) {
        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException("언어 코드는 비어있을 수 없습니다.");
        }
        this.language = language;
    }

    // === Getter ===
    public Long getId() { return id; }
    public Long getRoomId() { return roomId; }
    public String getGuestName() { return guestName; }
    public String getLanguage() { return language; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDate getCheckoutDate() { return checkoutDate; }
}
