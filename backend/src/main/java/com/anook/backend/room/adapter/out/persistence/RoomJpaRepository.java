package com.anook.backend.room.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Room JPA Repository — room 모듈 전용
 */
public interface RoomJpaRepository extends JpaRepository<RoomJpaEntity, Long> {
}
