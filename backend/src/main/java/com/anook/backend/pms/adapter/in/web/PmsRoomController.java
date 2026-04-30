package com.anook.backend.pms.adapter.in.web;

import com.anook.backend.pms.application.dto.response.GetPmsRoomResult;
import com.anook.backend.pms.application.port.in.GetPmsRoomUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * PMS 객실 관리 Controller
 */
@RestController
@RequestMapping("/pms/rooms")
@RequiredArgsConstructor
public class PmsRoomController {

    private final GetPmsRoomUseCase getPmsRoomUseCase;

    /** 전체 객실 목록 + 투숙 상태 조회 */
    @GetMapping
    public ResponseEntity<List<GetPmsRoomResult>> getAllRooms() {
        return ResponseEntity.ok(getPmsRoomUseCase.getAllRooms());
    }
}
