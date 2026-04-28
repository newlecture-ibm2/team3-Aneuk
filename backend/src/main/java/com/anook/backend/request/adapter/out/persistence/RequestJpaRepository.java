package com.anook.backend.request.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Request JPA Repository — request 모듈 전용
 */
public interface RequestJpaRepository extends JpaRepository<RequestJpaEntity, Long> {
}
