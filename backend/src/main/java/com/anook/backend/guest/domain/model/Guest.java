package com.anook.backend.guest.domain.model;

import java.time.LocalDateTime;

/**
 * 투숙객 도메인 모델 (순수 POJO — JPA 의존 없음)
 */
public class Guest {

    private Long id;
    private Long roomId;
    private String language;
    private String notes;
    private LocalDateTime createdAt;

    // === 기본 생성자 (프레임워크용) ===
    protected Guest() {}

    // === 전체 필드 생성자 (Entity → Domain 변환용) ===
    public Guest(Long id, Long roomId, String language, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.roomId = roomId;
        this.language = language;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    // === 팩토리 메서드 (체크인 시 사용) ===
    public static Guest create(Long roomId, String language) {
        Guest guest = new Guest();
        guest.roomId = roomId;
        guest.language = (language != null) ? language : "ko";
        guest.createdAt = LocalDateTime.now();
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
    public String getLanguage() { return language; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
