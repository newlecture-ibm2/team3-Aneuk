package com.anook.backend.security;

import com.anook.backend.security.dto.request.StaffLoginRequest;
import com.anook.backend.security.dto.response.LoginResponse;
import com.anook.backend.staff.application.port.out.StaffRepositoryPort;
import com.anook.backend.staff.domain.model.Staff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 직원/관리자 인증 서비스
 * PIN 검증 및 권한 기반 토큰 생성을 담당합니다.
 */
@Service
@Transactional(readOnly = true)
public class StaffAuthService {

    private final StaffRepositoryPort staffRepositoryPort;
    private final JwtProvider jwtProvider;

    public StaffAuthService(StaffRepositoryPort staffRepositoryPort, JwtProvider jwtProvider) {
        this.staffRepositoryPort = staffRepositoryPort;
        this.jwtProvider = jwtProvider;
    }

    /**
     * PIN 번호로 직원을 조회하고 인증을 수행합니다.
     */
    public LoginResponse login(StaffLoginRequest request) {
        // 1. PIN 번호로 직원 조회 (부서 정보 포함)
        Staff staff = staffRepositoryPort.findByPin(request.pin())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 PIN 번호입니다."));

        // 2. 부서의 isAdmin 여부에 따라 권한(Role) 결정
        String role = staff.getDepartment().isAdmin() ? "ADMIN" : "STAFF";

        // 3. JWT 토큰 생성 (Subject: PIN, Claim: Role)
        String token = jwtProvider.generateToken(staff.getPin(), role);

        // 4. 최종 응답 생성
        return LoginResponse.builder()
                .token(token)
                .role(role)
                .name(staff.getName())
                .department(staff.getDepartment().getName())
                .build();
    }
}
