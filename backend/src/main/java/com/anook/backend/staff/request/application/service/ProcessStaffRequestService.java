package com.anook.backend.staff.request.application.service;

import com.anook.backend.staff.request.application.port.in.ProcessStaffRequestUseCase;
import com.anook.backend.staff.request.application.port.out.StaffRequestRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * [직원 모듈] 요청 처리 서비스
 * 
 * 규칙: 타 도메인(request) 파일을 건드리지 않기 위해 staff 모듈 내부 어댑터를 사용하여 DB를 직접 업데이트함.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProcessStaffRequestService implements ProcessStaffRequestUseCase {

    private final StaffRequestRepositoryPort staffRequestRepository;

    @Override
    public void accept(Long id, String staffName) {
        staffRequestRepository.updateStatusWithStaff(id, "IN_PROGRESS", staffName);
    }

    @Override
    public void complete(Long id) {
        staffRequestRepository.updateStatus(id, "COMPLETED");
    }

    @Override
    public void reject(Long id) {
        staffRequestRepository.updateStatus(id, "PENDING"); // 거절 시 다시 대기 상태로
    }
}
