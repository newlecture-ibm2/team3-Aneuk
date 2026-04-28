package com.anook.backend.guest.application.port.in;

import com.anook.backend.guest.application.dto.request.CheckInGuestCommand;
import com.anook.backend.guest.application.dto.response.CheckInGuestResult;

/**
 * 투숙객 체크인 유스케이스
 */
public interface CheckInGuestUseCase {
    CheckInGuestResult checkIn(CheckInGuestCommand command);
}
