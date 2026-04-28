package com.anook.backend.room.application.dto.response;

import com.anook.backend.room.domain.model.Room;

/**
 * 객실 조회 응답 DTO
 */
public record GetRoomResult(
        Long id,
        String number,
        Long typeId
) {
    public static GetRoomResult from(Room room) {
        return new GetRoomResult(
                room.getId(),
                room.getNumber(),
                room.getTypeId()
        );
    }
}
