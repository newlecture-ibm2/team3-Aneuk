package com.anook.backend.pms.application.dto.response;

/**
 * PMS 객실 조회 결과 DTO
 */
public record GetPmsRoomResult(
        String number,
        String type,
        boolean occupied,
        String guestName
) {}
