package com.anook.backend.guest.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Guest JPA Repository — pms_guest 테이블
 */
public interface GuestJpaRepository extends JpaRepository<GuestJpaEntity, Long> {

    // === PMS 운영용 (체크인/체크아웃) ===
    boolean existsByRoomNumber(String roomNumber);
    Optional<GuestJpaEntity> findByRoomNumber(String roomNumber);

    // === ANOOK 인증용 (QR 로그인) ===
    Optional<GuestJpaEntity> findByAccessCode(String accessCode);
}
