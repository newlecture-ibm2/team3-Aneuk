package com.anook.backend.infrastructure.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * 모듈 간 통신 이벤트: AI가 고객 메시지에서 태스크형 요청을 감지했을 때 발행
 *
 * 발행자: message 도메인 (SendMessageService)
 * 수신자: request 도메인 (CreateRequestOnEventService → @EventListener)
 *
 * 이 클래스는 message ↔ request 간 유일한 공유 객체입니다.
 * 두 도메인이 서로를 직접 import하지 않고, 이 이벤트만 의존합니다.
 *
 * 소유: 🟦 Chat 담당이 정의 주도, 🟧 Request 담당은 읽기 전용
 */
@Getter
public class RequestDetectedEvent extends ApplicationEvent {

    /** 객실 번호 (예: "302") */
    private final String roomNo;

    /** 도메인 코드 (예: "HK", "FB", "FACILITY", "CONCIERGE", "FRONT", "EMERGENCY") */
    private final String domainCode;

    /** 우선순위 (예: "NORMAL", "HIGH", "URGENT") */
    private final String priority;

    /** 요청 의도 (예: "SUPPLY_REQUEST", "ROOM_SERVICE", "REPAIR_REQUEST") */
    private final String intent;

    /** 부서별 가변 데이터 (예: {"item": "towel", "qty": 2}) */
    private final Map<String, Object> entities;

    /** AI 확신도 (0.0 ~ 1.0) */
    private final double confidence;

    /** 고객 발화 원문 (예: "수건 2장 주세요") */
    private final String rawText;

    /** AI가 생성한 요약 (예: "수건 2장 요청") */
    private final String summary;

    /** 에스컬레이션 여부 (confidence < 0.7이면 true) */
    private final boolean escalated;

    public RequestDetectedEvent(Object source,
                                 String roomNo,
                                 String domainCode,
                                 String priority,
                                 String intent,
                                 Map<String, Object> entities,
                                 double confidence,
                                 String rawText,
                                 String summary,
                                 boolean escalated) {
        super(source);
        this.roomNo = roomNo;
        this.domainCode = domainCode;
        this.priority = priority;
        this.intent = intent;
        this.entities = entities;
        this.confidence = confidence;
        this.rawText = rawText;
        this.summary = summary;
        this.escalated = escalated;
    }
}
