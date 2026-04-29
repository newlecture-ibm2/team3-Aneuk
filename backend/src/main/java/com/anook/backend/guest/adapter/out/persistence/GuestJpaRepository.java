package com.anook.backend.guest.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Guest JPA Repository — pms_guest 테이블
 */
public interface GuestJpaRepository extends JpaRepository<GuestJpaEntity, Long> {

    boolean existsByRoomNumber(String roomNumber);

    Optional<GuestJpaEntity> findByRoomNumber(String roomNumber);
}
