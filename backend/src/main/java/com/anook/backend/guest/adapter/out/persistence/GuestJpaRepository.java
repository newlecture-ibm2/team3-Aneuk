package com.anook.backend.guest.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Guest JPA Repository — Adapter 계층에서만 사용 (Service에서 직접 import 금지)
 */
public interface GuestJpaRepository extends JpaRepository<GuestJpaEntity, Long> {

    boolean existsByRoomId(Long roomId);

    java.util.Optional<GuestJpaEntity> findByRoomId(Long roomId);
}
