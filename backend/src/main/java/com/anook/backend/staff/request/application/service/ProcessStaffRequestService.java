package com.anook.backend.staff.request.application.service;

import com.anook.backend.staff.request.application.port.in.ProcessStaffRequestUseCase;
import com.anook.backend.request.application.port.in.UpdateStaffRequestUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * [직원 모듈] 요청 처리 서비스
 * 
 * 규칙: 쓰기 작업은 해당 테이블을 소유한 모듈(request)의 Port(In)을 호출함.
 */
@Service
@RequiredArgsConstructor
public class ProcessStaffRequestService implements ProcessStaffRequestUseCase {

    private final UpdateStaffRequestUseCase updateStaffRequestUseCase;

    @Override
    public void accept(Long id, String staffName) {
        updateStaffRequestUseCase.accept(id, staffName);
    }

    @Override
    public void complete(Long id) {
        updateStaffRequestUseCase.complete(id);
    }

    @Override
    public void reject(Long id) {
        updateStaffRequestUseCase.reject(id);
    }
}
