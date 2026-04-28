package com.anook.backend.guest.adapter.out.persistence;

import com.anook.backend.guest.domain.model.Guest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * Guest JPA Entity — DB 테이블 매핑 전용 (외부 노출 금지)
 */
@Entity
@Table(name = "guest")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuestJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false, unique = true)
    private Long roomId;

    @Column(nullable = false)
    private String language;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // === Domain → Entity ===
    public static GuestJpaEntity from(Guest domain) {
        GuestJpaEntity entity = new GuestJpaEntity();
        entity.id = domain.getId();
        entity.roomId = domain.getRoomId();
        entity.language = domain.getLanguage();
        entity.notes = domain.getNotes();
        entity.createdAt = domain.getCreatedAt();
        return entity;
    }

    // === Entity → Domain ===
    public Guest toDomain() {
        return new Guest(id, roomId, language, notes, createdAt);
    }
}
