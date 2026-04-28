package com.anook.backend.guest.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * 체크인 요청 DTO
 */
public record CheckInGuestCommand(
        @NotNull(message = "객실 ID는 필수입니다.")
        Long roomId,

        @NotBlank(message = "투숙객 이름은 필수입니다.")
        String guestName,

        String language,

        @NotNull(message = "예정 체크아웃 날짜는 필수입니다.")
        LocalDate checkoutDate
) {}
