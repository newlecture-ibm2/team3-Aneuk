package com.anook.backend.message.application.service;

import com.anook.backend.message.application.dto.response.MessageDto;
import com.anook.backend.message.application.port.in.GetMessageHistoryUseCase;
import com.anook.backend.message.application.port.out.MessageRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 대화 내역 조회 서비스
 *
 * roomNo → roomId 변환 후 시간순 메시지 목록 반환
 *
 * ❌ JPA Repository 직접 import 금지 → Port(Out)만 의존
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GetMessageHistoryService implements GetMessageHistoryUseCase {

    private final MessageRepositoryPort messagePort;

    @Override
    @Transactional(readOnly = true)
    public List<MessageDto> getHistory(String roomNo) {
        Long roomId = messagePort.findRoomIdByRoomNo(roomNo)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 객실입니다: " + roomNo));

        return messagePort.findByRoomIdOrderByCreatedAt(roomId)
                .stream()
                .map(MessageDto::from)
                .toList();
    }
}
