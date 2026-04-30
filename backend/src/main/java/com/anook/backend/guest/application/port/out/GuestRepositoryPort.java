package com.anook.backend.guest.application.port.out;

import com.anook.backend.guest.domain.model.Guest;

import java.util.List;
import java.util.Optional;

/**
 * Guest 영속성 포트 — 도메인 모델만 반환 (PMS 전용)
 */
public interface GuestRepositoryPort {

    Guest save(Guest guest);

    Optional<Guest> findById(Long id);

    Optional<Guest> findByRoomNumber(String roomNumber);

    List<Guest> findAll();

    void deleteById(Long id);

    boolean existsByRoomNumber(String roomNumber);
}
