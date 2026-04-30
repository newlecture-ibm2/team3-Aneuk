package com.anook.backend.security.dto.request;

/**
 * 투숙객 로그인 요청 DTO
 * QR 코드에 담긴 랜덤한 접속 코드(accessCode)를 전달합니다.
 */
public record GuestLoginRequest(String accessCode) {
}
