package com.anook.backend.room.adapter.out.persistence;

import com.anook.backend.room.domain.model.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Room JPA Entity — room 모듈 전용
 */
@Entity
@Table(name = "room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(name = "type_id", nullable = false)
    private Long typeId;

    // === Entity → Domain ===
    public Room toDomain() {
        return new Room(id, number, typeId);
    }
}
