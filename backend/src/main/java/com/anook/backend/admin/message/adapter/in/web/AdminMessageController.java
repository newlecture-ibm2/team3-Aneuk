package com.anook.backend.admin.message.adapter.in.web;

import com.anook.backend.message.application.dto.response.MessageDto;
import com.anook.backend.message.application.port.in.GetMessageHistoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 관리자 메시지 히스토리 Controller
 *
 * 기존 message 모듈의 GetMessageHistoryUseCase를 호출하여
 * 관리자가 모든 객실의 메시지 내역을 조회할 수 있도록 합니다.
 *
 * 도메인 분리 없이 Controller만 존재 (읽기 전용).
 */
@RestController
@RequestMapping("/admin/messages")
@RequiredArgsConstructor
public class AdminMessageController {

    private final GetMessageHistoryUseCase getMessageHistoryUseCase;
    private final JdbcTemplate jdbcTemplate;

    /**
     * 메시지가 있는 객실 목록 조회
     *
     * GET /admin/messages/rooms
     * 응답: [{ "roomId": 1, "roomNo": "101" }, ...]
     */
    @GetMapping("/rooms")
    public ResponseEntity<List<Map<String, Object>>> getMessageRooms() {
        List<Long> roomIds = getMessageHistoryUseCase.getChatRoomIds();

        if (roomIds.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        // roomId → roomNo 매핑 (room 테이블 읽기 전용)
        String inClause = roomIds.stream()
                .map(String::valueOf)
                .reduce((a, b) -> a + "," + b)
                .orElse("");

        List<Map<String, Object>> rooms = jdbcTemplate.queryForList(
                "SELECT id AS \"roomId\", number AS \"roomNo\" FROM room WHERE id IN (" + inClause + ") ORDER BY number"
        );

        return ResponseEntity.ok(rooms);
    }

    /**
     * 특정 객실의 메시지 목록 조회
     *
     * GET /admin/messages/rooms/{roomNo}/messages
     */
    @GetMapping("/rooms/{roomNo}/messages")
    public ResponseEntity<List<MessageDto>> getRoomMessages(@PathVariable String roomNo) {
        return ResponseEntity.ok(getMessageHistoryUseCase.getHistory(roomNo));
    }
}
