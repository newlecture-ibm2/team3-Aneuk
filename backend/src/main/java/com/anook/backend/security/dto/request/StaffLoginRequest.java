package com.anook.backend.security.dto.request;

/**
 * 직원 로그인 요청 DTO
 * PIN 번호만으로 로그인을 시도합니다.
 */
public record StaffLoginRequest(String pin) {
}
