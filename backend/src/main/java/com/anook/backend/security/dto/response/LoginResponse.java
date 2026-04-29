package com.anook.backend.security.dto.response;

import lombok.Builder;

/**
 * 로그인 성공 응답 DTO
 * JWT 토큰과 함께 프론트엔드 UI 표시에 필요한 정보를 포함합니다.
 */
@Builder
public record LoginResponse(
    String token,      // JWT 토큰
    String role,       // 권한 (ADMIN, STAFF, GUEST)
    String name,       // 사용자 이름
    String department  // 소속 부서명
) {
}
