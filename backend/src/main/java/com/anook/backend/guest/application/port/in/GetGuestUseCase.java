package com.anook.backend.guest.application.port.in;

import com.anook.backend.guest.application.dto.response.GetGuestResult;

import java.util.List;

/**
 * 투숙객 조회 유스케이스
 */
public interface GetGuestUseCase {
    List<GetGuestResult> getAll();
}
