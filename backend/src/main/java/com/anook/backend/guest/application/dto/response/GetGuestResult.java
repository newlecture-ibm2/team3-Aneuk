package com.anook.backend.guest.application.dto.response;

import com.anook.backend.guest.domain.model.Guest;

import java.time.LocalDateTime;

/**
 * 투숙객 조회 응답 DTO
 */
public record GetGuestResult(
        Long id,
        String roomNumber,
        String language,
        LocalDateTime createdAt
) {
    public static GetGuestResult of(Guest guest, String roomNumber) {
        return new GetGuestResult(
                guest.getId(),
                roomNumber,
                guest.getLanguage(),
                guest.getCreatedAt()
        );
    }
}
