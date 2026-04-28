package com.anook.backend.guest.application.dto.response;

import com.anook.backend.guest.domain.model.Guest;

import java.time.LocalDateTime;

/**
 * 체크인 응답 DTO
 */
public record CheckInGuestResult(
        Long guestId,
        String roomNumber,
        String language,
        LocalDateTime createdAt
) {
    public static CheckInGuestResult of(Guest guest, String roomNumber) {
        return new CheckInGuestResult(
                guest.getId(),
                roomNumber,
                guest.getLanguage(),
                guest.getCreatedAt()
        );
    }
}
