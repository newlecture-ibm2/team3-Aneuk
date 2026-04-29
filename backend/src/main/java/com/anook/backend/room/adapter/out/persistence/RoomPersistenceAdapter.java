package com.anook.backend.room.adapter.out.persistence;

import com.anook.backend.room.application.port.out.RoomRepositoryPort;
import com.anook.backend.room.domain.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * RoomRepositoryPort 구현체 — Room 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class RoomPersistenceAdapter implements RoomRepositoryPort {

    private final RoomJpaRepository jpaRepository;

    @Override
    public Optional<Room> findByNumber(String number) {
        return jpaRepository.findById(number).map(RoomJpaEntity::toDomain);
    }

    @Override
    public List<Room> findAll() {
        return jpaRepository.findAll().stream()
                .map(RoomJpaEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existsByNumber(String number) {
        return jpaRepository.existsById(number);
    }

    @Override
    public Room save(Room room) {
        RoomJpaEntity entity = RoomJpaEntity.from(room);
        RoomJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public void deleteByNumber(String number) {
        jpaRepository.deleteById(number);
    }
}
