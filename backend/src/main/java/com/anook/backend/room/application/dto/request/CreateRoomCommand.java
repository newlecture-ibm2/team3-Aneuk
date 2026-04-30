package com.anook.backend.room.application.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 객실 등록 요청 DTO — ANOOK은 호실 번호만 관리
 */
public record CreateRoomCommand(
        @NotBlank(message = "객실 번호는 필수입니다.")
        String number
) {}
