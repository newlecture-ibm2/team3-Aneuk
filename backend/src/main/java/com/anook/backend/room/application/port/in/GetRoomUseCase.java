package com.anook.backend.room.application.port.in;

import com.anook.backend.room.application.dto.response.GetRoomResult;

import java.util.List;

/**
 * 객실 조회 유스케이스
 */
public interface GetRoomUseCase {
    List<GetRoomResult> getAll();
}
