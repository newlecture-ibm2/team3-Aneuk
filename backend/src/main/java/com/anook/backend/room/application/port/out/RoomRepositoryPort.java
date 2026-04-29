package com.anook.backend.room.application.port.out;

import com.anook.backend.room.domain.model.Room;

import java.util.List;
import java.util.Optional;

/**
 * Room 영속성 포트 — 도메인 모델만 반환
 */
public interface RoomRepositoryPort {

    Optional<Room> findById(Long id);

    List<Room> findAll();

    boolean existsById(Long id);

    Room save(Room room);

    void deleteById(Long id);

    boolean existsByNumber(String number);
}
