package com.anook.backend.message.application.port.in;

import com.anook.backend.message.application.dto.response.MessageDto;

import java.util.List;

/**
 * 대화 내역 조회 유스케이스 (Port In)
 *
 * Controller가 의존하는 인터페이스.
 * 구현체: GetMessageHistoryService
 */
public interface GetMessageHistoryUseCase {

    /**
     * 특정 객실의 대화 내역을 시간순으로 조회한다.
     *
     * @param roomNo 객실 번호 (예: "707")
     * @return 메시지 목록 (시간순 정렬)
     */
    List<MessageDto> getHistory(String roomNo);

    /**
     * 메시지가 존재하는 객실 ID 목록 (관리자 채팅 히스토리용)
     */
    List<Long> getChatRoomIds();
}
