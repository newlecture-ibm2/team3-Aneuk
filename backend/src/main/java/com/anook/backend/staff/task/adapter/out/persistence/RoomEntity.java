package com.anook.backend.staff.task.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * room 테이블 엔티티 — staff/task 모듈 전용
 */
@Entity
@Table(name = "room")
@Getter
@NoArgsConstructor
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(nullable = false)
    private Integer floor;

    @Column(name = "type_id", nullable = false)
    private String typeId;
}
