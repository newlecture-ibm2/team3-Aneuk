package com.anook.backend.security;

import com.anook.backend.security.dto.request.StaffLoginRequest;
import com.anook.backend.security.dto.response.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 직원/관리자 인증 컨트롤러
 * (BFF 프록시 패턴에 따라 /api 접두어를 사용하지 않습니다.)
 */
@RestController
@RequestMapping("/auth/staff")
public class StaffAuthController {

    private final StaffAuthService staffAuthService;

    public StaffAuthController(StaffAuthService staffAuthService) {
        this.staffAuthService = staffAuthService;
    }

    /**
     * 직원 로그인 API
     * 성공 시 토큰과 유저 정보를 JSON으로 반환합니다.
     */
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody StaffLoginRequest request) {
        LoginResponse response = staffAuthService.login(request);
        return ResponseEntity.ok(response);
    }
}
