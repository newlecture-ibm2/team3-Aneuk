package com.anook.backend.guest.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import java.util.Optional;

/**
 * Guest JPA Repository (Spring Data JPA)
 */
public interface GuestJpaRepository extends JpaRepository<GuestJpaEntity, Long> {
    Optional<GuestJpaEntity> findByRoomId(Long roomId);
    Optional<GuestJpaEntity> findByAccessCode(String accessCode); // 랜덤 코드로 조회 추가
    boolean existsByRoomId(Long roomId);
 * Guest JPA Repository — pms_guest 테이블
 */
public interface GuestJpaRepository extends JpaRepository<GuestJpaEntity, Long> {

    boolean existsByRoomNumber(String roomNumber);

    Optional<GuestJpaEntity> findByRoomNumber(String roomNumber);
}
