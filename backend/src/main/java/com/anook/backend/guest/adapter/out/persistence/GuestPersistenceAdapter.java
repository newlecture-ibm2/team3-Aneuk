package com.anook.backend.guest.adapter.out.persistence;

import com.anook.backend.guest.application.port.out.GuestRepositoryPort;
import com.anook.backend.guest.domain.model.Guest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Guest 영속성 어댑터 — GuestRepositoryPort 구현체 (PMS 전용)
 */
@Component
public class GuestPersistenceAdapter implements GuestRepositoryPort {

    private final GuestJpaRepository guestJpaRepository;

    public GuestPersistenceAdapter(GuestJpaRepository guestJpaRepository) {
        this.guestJpaRepository = guestJpaRepository;
    }

    @Override
    public Guest save(Guest guest) {
        GuestJpaEntity entity = GuestJpaEntity.from(guest);
        return guestJpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Guest> findById(Long id) {
        return guestJpaRepository.findById(id).map(GuestJpaEntity::toDomain);
    }

    @Override
    public Optional<Guest> findByRoomNumber(String roomNumber) {
        return guestJpaRepository.findByRoomNumber(roomNumber).map(GuestJpaEntity::toDomain);
    }

    @Override
    public Optional<Guest> findByAccessCode(String accessCode) {
        return guestJpaRepository.findByAccessCode(accessCode).map(GuestJpaEntity::toDomain);
    }

    @Override
    public List<Guest> findAll() {
        return guestJpaRepository.findAll().stream()
                .map(GuestJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        guestJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByRoomNumber(String roomNumber) {
        return guestJpaRepository.existsByRoomNumber(roomNumber);
    }
}
