package com.anook.backend.pms.application.port.in;

import com.anook.backend.pms.application.dto.response.GetPmsRoomResult;

import java.util.List;

/**
 * PMS 객실 목록 조회 UseCase
 */
public interface GetPmsRoomUseCase {

    List<GetPmsRoomResult> getAllRooms();
}
