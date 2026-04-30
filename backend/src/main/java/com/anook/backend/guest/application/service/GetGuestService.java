package com.anook.backend.guest.application.service;

import com.anook.backend.guest.application.dto.response.GetGuestResult;
import com.anook.backend.guest.application.port.in.GetGuestUseCase;
import com.anook.backend.guest.application.port.out.GuestRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * PMS 투숙객 목록 조회 서비스
 *
 * ❌ JPA Repository 직접 import 금지
 * ✅ Port 인터페이스만 의존
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetGuestService implements GetGuestUseCase {

    private final GuestRepositoryPort guestRepository;

    @Override
    public List<GetGuestResult> getAll() {
        return guestRepository.findAll().stream()
                .map(GetGuestResult::from)
                .toList();
    }
}
