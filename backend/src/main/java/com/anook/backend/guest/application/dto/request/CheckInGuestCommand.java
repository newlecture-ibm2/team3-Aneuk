package com.anook.backend.guest.application.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * 체크인 요청 DTO
 */
public record CheckInGuestCommand(
        @NotNull(message = "객실 ID는 필수입니다.")
        Long roomId,

        String language
) {}
