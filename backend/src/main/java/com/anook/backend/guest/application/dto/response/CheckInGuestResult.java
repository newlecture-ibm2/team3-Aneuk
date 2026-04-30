package com.anook.backend.guest.application.dto.response;

import com.anook.backend.guest.domain.model.Guest;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * PMS 체크인 응답 DTO
 */
public record CheckInGuestResult(
        Long guestId,
        String roomNumber,
        String name,
        String phone,
        LocalDateTime checkinDate,
        LocalDate checkoutDate
) {
    public static CheckInGuestResult from(Guest guest) {
        return new CheckInGuestResult(
                guest.getId(),
                guest.getRoomNumber(),
                guest.getName(),
                guest.getPhone(),
                guest.getCheckinDate(),
                guest.getCheckoutDate()
        );
    }
}
