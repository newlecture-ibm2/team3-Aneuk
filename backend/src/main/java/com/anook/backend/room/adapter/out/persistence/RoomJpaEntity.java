package com.anook.backend.room.adapter.out.persistence;

import com.anook.backend.room.domain.model.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Room JPA Entity — ANOOK room 테이블 (number PK)
 */
@Entity
@Table(name = "room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomJpaEntity {

    @Id
    @Column(length = 10)
    private String number;

    // === Domain → Entity ===
    public static RoomJpaEntity from(Room domain) {
        RoomJpaEntity entity = new RoomJpaEntity();
        entity.number = domain.getNumber();
        return entity;
    }

    // === Entity → Domain ===
    public Room toDomain() {
        return new Room(number);
    }
}
