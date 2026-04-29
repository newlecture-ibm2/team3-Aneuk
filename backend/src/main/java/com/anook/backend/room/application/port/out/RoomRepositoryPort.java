package com.anook.backend.room.application.port.out;

import com.anook.backend.room.domain.model.Room;

import java.util.List;
import java.util.Optional;

/**
 * Room 영속성 포트 — 도메인 모델만 반환
 */
public interface RoomRepositoryPort {

    Optional<Room> findByNumber(String number);

    List<Room> findAll();

    boolean existsByNumber(String number);

    Room save(Room room);

    void deleteByNumber(String number);
}
