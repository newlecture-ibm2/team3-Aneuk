package com.anook.backend.request.adapter.out.persistence;

import com.anook.backend.request.application.port.out.RequestRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * RequestRepositoryPort 구현체 — Request 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class RequestPersistenceAdapter implements RequestRepositoryPort {

    private final RequestJpaRepository jpaRepository;

    @Override
    public Optional<RequestStatusDto> findStatusById(Long id) {
        return jpaRepository.findById(id)
                .map(entity -> new RequestStatusDto(
                        entity.getId(),
                        entity.getStatus(),
                        entity.getDepartmentId(),
                        entity.getSummary()
                ));
    }

    @Override
    public void updateStatus(Long id, String status) {
        RequestJpaEntity entity = jpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + id));
        entity.updateStatus(status);
        jpaRepository.save(entity);
    }
}
