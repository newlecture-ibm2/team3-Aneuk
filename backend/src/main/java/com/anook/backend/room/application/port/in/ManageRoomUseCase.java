package com.anook.backend.room.application.port.in;

import com.anook.backend.room.application.dto.request.CreateRoomCommand;
import com.anook.backend.room.application.dto.response.GetRoomResult;

import java.util.List;

/**
 * 객실 관리 유스케이스
 */
public interface ManageRoomUseCase {

    List<GetRoomResult> getAll();

    GetRoomResult create(CreateRoomCommand command);

    void delete(String roomNumber);
}
