package com.anook.backend.room.adapter.in.web;

import com.anook.backend.room.application.dto.response.GetRoomResult;
import com.anook.backend.room.application.port.in.GetRoomUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 객실 조회 Controller
 */
@RestController
@RequestMapping("/admin/rooms")
@RequiredArgsConstructor
public class AdminRoomController {

    private final GetRoomUseCase getRoomUseCase;

    /**
     * 전체 객실 목록 조회 — GET /admin/rooms
     */
    @GetMapping
    public ResponseEntity<List<GetRoomResult>> getRooms() {
        return ResponseEntity.ok(getRoomUseCase.getAll());
    }
}
