package com.anook.backend.request.application.service;

import com.anook.backend.request.application.port.in.UpdateStaffRequestUseCase;
import com.anook.backend.request.application.port.out.RequestRepositoryPort;
import com.anook.backend.request.domain.model.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * [요청 모듈] 상태 변경 서비스
 * 
 * 규칙: 타 모듈(staff)에서 쓰기 요청이 올 때 이 서비스를 통해 도메인 로직을 처리함.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateStaffRequestService implements UpdateStaffRequestUseCase {

    private final RequestRepositoryPort requestRepository;

    @Override
    public void accept(Long id, String staffName) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("요청을 찾을 수 없습니다: " + id));
        request.accept(staffName);
        requestRepository.save(request);
    }

    @Override
    public void complete(Long id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("요청을 찾을 수 없습니다: " + id));
        request.complete();
        requestRepository.save(request);
    }

    @Override
    public void reject(Long id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("요청을 찾을 수 없습니다: " + id));
        request.reject();
        requestRepository.save(request);
    }
}
