package com.anook.backend.message.adapter.in.web;

import com.anook.backend.message.adapter.in.web.dto.request.SendMessageRequest;
import com.anook.backend.message.application.dto.request.SendMessageCommand;
import com.anook.backend.message.application.dto.response.MessageDto;
import com.anook.backend.message.application.dto.response.SendMessageResult;
import com.anook.backend.message.application.port.in.GetMessageHistoryUseCase;
import com.anook.backend.message.application.port.in.SendMessageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 고객 메시지 컨트롤러
 *
 * ❌ 비즈니스 로직 처리 금지 — UseCase에 위임
 * ❌ @RequestMapping에 /api 접두어 금지 (BFF가 제거하고 전달)
 * ❌ @RequestBody Map 금지 — 전용 Request DTO 사용
 */
@RestController
@RequestMapping("/chat/{roomNo}/messages")
@RequiredArgsConstructor
public class GuestMessageController {

    private final SendMessageUseCase sendMessageUseCase;
    private final GetMessageHistoryUseCase getMessageHistoryUseCase;

    /**
     * 고객 메시지 전송
     *
     * POST /chat/{roomNo}/messages
     * Body: { "content": "수건 2장 주세요" }
     */
    @PostMapping
    public ResponseEntity<SendMessageResult> sendMessage(
            @PathVariable String roomNo,
            @RequestBody SendMessageRequest request
    ) {
        if (request.content() == null || request.content().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        SendMessageCommand command = new SendMessageCommand(roomNo, request.content());
        SendMessageResult result = sendMessageUseCase.send(command);

        return ResponseEntity.ok(result);
    }

    /**
     * 대화 내역 조회
     *
     * GET /chat/{roomNo}/messages
     */
    @GetMapping
    public ResponseEntity<List<MessageDto>> getHistory(@PathVariable String roomNo) {
        List<MessageDto> messages = getMessageHistoryUseCase.getHistory(roomNo);
        return ResponseEntity.ok(messages);
    }
}
