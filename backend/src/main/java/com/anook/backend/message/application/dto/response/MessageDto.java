package com.anook.backend.message.application.dto.response;

import com.anook.backend.message.domain.model.Message;

import java.time.LocalDateTime;

/**
 * 메시지 조회 결과 DTO (Service → Controller)
 *
 * 도메인 모델의 필요한 필드만 노출
 */
public record MessageDto(
        Long id,
        String senderType,
        String content,
        String translatedContent,
        LocalDateTime createdAt
) {
    public static MessageDto from(Message msg) {
        return new MessageDto(
                msg.getId(),
                msg.getSenderType().name(),
                msg.getContent(),
                msg.getTranslatedContent(),
                msg.getCreatedAt()
        );
    }
}
