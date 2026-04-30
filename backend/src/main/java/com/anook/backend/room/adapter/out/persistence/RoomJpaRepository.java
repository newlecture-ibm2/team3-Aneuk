package com.anook.backend.room.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Room JPA Repository — PK가 String(number)
 */
public interface RoomJpaRepository extends JpaRepository<RoomJpaEntity, String> {
}
