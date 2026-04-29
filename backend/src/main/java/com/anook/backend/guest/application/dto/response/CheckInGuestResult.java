package com.anook.backend.guest.application.dto.response;

import com.anook.backend.guest.domain.model.Guest;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 체크인 응답 DTO
 */
public record CheckInGuestResult(
        Long guestId,
        String roomNumber,
        String guestName,
        String language,
        LocalDateTime createdAt,
        LocalDate checkoutDate
) {
    public static CheckInGuestResult of(Guest guest, String roomNumber) {
        return new CheckInGuestResult(
                guest.getId(),
                roomNumber,
                guest.getGuestName(),
                guest.getLanguage(),
                guest.getCreatedAt(),
                guest.getCheckoutDate()
        );
    }
}
