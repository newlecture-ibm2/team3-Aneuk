package com.anook.backend.guest.application.dto.response;

import com.anook.backend.guest.domain.model.Guest;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * PMS 투숙객 조회 응답 DTO
 */
public record GetGuestResult(
        Long id,
        String roomNumber,
        String name,
        String phone,
        LocalDateTime checkinDate,
        LocalDate checkoutDate
) {
    public static GetGuestResult from(Guest guest) {
        return new GetGuestResult(
                guest.getId(),
                guest.getRoomNumber(),
                guest.getName(),
                guest.getPhone(),
                guest.getCheckinDate(),
                guest.getCheckoutDate()
        );
    }
}
