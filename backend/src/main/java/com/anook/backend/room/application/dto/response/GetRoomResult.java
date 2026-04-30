package com.anook.backend.room.application.dto.response;

import com.anook.backend.room.domain.model.Room;

/**
 * 객실 조회 응답 DTO — 호실 번호만
 */
public record GetRoomResult(
        String number
) {
    public static GetRoomResult from(Room room) {
        return new GetRoomResult(room.getNumber());
    }
}
