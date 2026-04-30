package com.anook.backend.guest.adapter.out.persistence;

import com.anook.backend.guest.application.port.out.GuestRepositoryPort;
import com.anook.backend.guest.domain.model.Guest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Guest 영속성 어댑터 — GuestRepositoryPort 구현체 (PMS 전용)
 */
@Component
@RequiredArgsConstructor
public class GuestPersistenceAdapter implements GuestRepositoryPort {

    private final GuestJpaRepository jpaRepository;

    @Override
    public Guest save(Guest guest) {
        GuestJpaEntity entity = GuestJpaEntity.from(guest);
        GuestJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Guest> findById(Long id) {
        return jpaRepository.findById(id).map(GuestJpaEntity::toDomain);
    }

    @Override
    public Optional<Guest> findByRoomNumber(String roomNumber) {
        return jpaRepository.findByRoomNumber(roomNumber).map(GuestJpaEntity::toDomain);
    }

    @Override
    public List<Guest> findAll() {
        return jpaRepository.findAll().stream()
                .map(GuestJpaEntity::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByRoomNumber(String roomNumber) {
        return jpaRepository.existsByRoomNumber(roomNumber);
    }
}
