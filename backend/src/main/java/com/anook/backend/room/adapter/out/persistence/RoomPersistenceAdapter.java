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
    public Optional<Room> findById(Long id) {
        return jpaRepository.findById(id).map(RoomJpaEntity::toDomain);
    }

    @Override
    public List<Room> findAll() {
        return jpaRepository.findAll().stream()
                .map(RoomJpaEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
