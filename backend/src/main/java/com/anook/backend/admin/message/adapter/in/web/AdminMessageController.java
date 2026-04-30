package com.anook.backend.admin.message.adapter.in.web;

import com.anook.backend.admin.message.application.port.out.AdminMessageQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 관리자 메시지 히스토리 Controller
 *
 * admin/message 모듈의 자체 Port를 통해 message 테이블을 조회합니다.
 * message 모듈의 UseCase/Port를 import하지 않고 독립적으로 동작합니다.
 */
@RestController
@RequestMapping("/admin/messages")
@RequiredArgsConstructor
public class AdminMessageController {

    private final AdminMessageQueryPort adminMessageQueryPort;

    /**
     * 메시지가 있는 객실 목록 조회
     *
     * GET /admin/messages/rooms
     * 응답: [{ "roomId": 1, "roomNo": "101" }, ...]
     */
    @GetMapping("/rooms")
    public ResponseEntity<List<Map<String, Object>>> getMessageRooms() {
        return ResponseEntity.ok(adminMessageQueryPort.findRoomsWithMessages());
    }

    /**
     * 특정 객실의 메시지 목록 조회
     *
     * GET /admin/messages/rooms/{roomNo}/messages
     */
    @GetMapping("/rooms/{roomNo}/messages")
    public ResponseEntity<List<Map<String, Object>>> getRoomMessages(@PathVariable String roomNo) {
        return ResponseEntity.ok(adminMessageQueryPort.findMessagesByRoomNo(roomNo));
    }
}
