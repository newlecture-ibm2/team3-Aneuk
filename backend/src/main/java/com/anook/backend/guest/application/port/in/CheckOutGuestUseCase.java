package com.anook.backend.guest.application.port.in;

/**
 * 투숙객 체크아웃 유스케이스 (Hard Delete)
 */
public interface CheckOutGuestUseCase {
    void checkOut(Long guestId);
}
