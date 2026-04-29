package com.anook.backend.guest.application.port.out;

import com.anook.backend.guest.domain.model.Guest;

import java.util.List;
import java.util.Optional;

/**
 * Guest 영속성 포트 — 도메인 모델만 반환 (JPA Entity 반환 금지)
 */
public interface GuestRepositoryPort {

    Guest save(Guest guest);

    Optional<Guest> findById(Long id);

    Optional<Guest> findByRoomId(Long roomId);

    List<Guest> findAll();

    void deleteById(Long id);

    boolean existsByRoomId(Long roomId);
}
