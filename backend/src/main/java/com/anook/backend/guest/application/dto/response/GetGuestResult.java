package com.anook.backend.guest.application.dto.response;

import com.anook.backend.guest.domain.model.Guest;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 투숙객 조회 응답 DTO
 */
public record GetGuestResult(
        Long id,
        String roomNumber,
        String guestName,
        String language,
        LocalDateTime createdAt,
        LocalDate checkoutDate
) {
    public static GetGuestResult of(Guest guest, String roomNumber) {
        return new GetGuestResult(
                guest.getId(),
                roomNumber,
                guest.getGuestName(),
                guest.getLanguage(),
                guest.getCreatedAt(),
                guest.getCheckoutDate()
        );
    }
}
