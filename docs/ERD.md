# 아늑(Aneuk) ERD

```mermaid
erDiagram
    department {
        bigint id PK
        varchar code UK "HK, FB, ENG, FRONT"
        varchar name "하우스키핑, 식음료, 시설, 프론트"
    }

    room {
        bigint id PK
        varchar number UK "101, 302, 501"
        int floor
        varchar type "STANDARD, DELUXE, SUITE"
    }

    staff {
        bigint id PK
        varchar name
        varchar pin "4자리 PIN"
        varchar role "STAFF, MANAGER"
        bigint department_id FK
    }

    guest {
        bigint id PK
        varchar qr_token UK "QR 인증 토큰"
        bigint room_id FK
        timestamp created_at "체크인 시각"
    }

    request {
        bigint id PK
        varchar status "PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED"
        varchar priority "LOW, NORMAL, HIGH, URGENT"
        varchar department_code "HK, FB, ENG"
        varchar item_code "TOWEL, WATER, ETC"
        int quantity
        text raw_text "고객 발화 원문"
        text summary "AI 요약"
        float confidence "AI 확신도 0.0~1.0"
        bigint room_id FK
        bigint assigned_staff_id FK "nullable"
        bigint department_id FK
        int version "낙관적 락"
        timestamp created_at
        timestamp updated_at
    }

    message {
        bigint id PK
        varchar sender_type "GUEST, AI, STAFF"
        text content "원문"
        text translated_content "번역문"
        bigint room_id FK
        bigint request_id FK "nullable"
        timestamp created_at
    }

    knowledge_entry {
        bigint id PK
        text question "질문"
        text answer "답변"
        vector embedding "pgvector 임베딩"
        varchar status "PENDING, APPROVED, REJECTED"
        bigint approved_by FK "nullable, staff_id"
        timestamp created_at
        timestamp updated_at
    }

    unanswered_question {
        bigint id PK
        text question "고객 원문 질문"
        varchar cluster_id "nullable, 군집 ID"
        text suggested_answer "nullable, AI 생성 답변 초안"
        varchar status "NEW, CLUSTERED, PROMOTED, DISMISSED"
        timestamp created_at
    }

    handover_briefing {
        bigint id PK
        timestamp shift_start "교대 시작 시각"
        timestamp shift_end "교대 종료 시각"
        int total_request_count "해당 시간대 총 요청 건수"
        int pending_count "미처리 건수"
        int escalated_count "에스컬레이션 건수"
        text summary "AI 자동 생성 브리핑 요약"
        timestamp created_at
    }

    dispatch_log {
        bigint id PK
        varchar target "수신 대상 (staffId, deptCode 등)"
        varchar event_type "NEW_REQUEST, STATUS_CHANGED, ESCALATED"
        text payload "JSON"
        timestamp sent_at
    }

    %% ===== 관계 =====

    department ||--o{ staff : "소속"
    department ||--o{ request : "담당 부서"

    room ||--o| guest : "투숙 중"
    room ||--o{ request : "요청 발생"
    room ||--o{ message : "대화 발생"

    staff ||--o{ request : "배정된 직원"

    request ||--o{ message : "관련 대화"
```

## 테이블 요약

| 테이블 | 설명 | 레코드 수명 |
|--------|------|-----------|
| **department** | 부서 (HK, FB, ENG, FRONT) | 영구 |
| **room** | 객실 정보 | 영구 |
| **staff** | 직원 (PIN 로그인) | 영구 |
| **guest** | QR 토큰 ↔ 객실 매핑 (인증 전용) | 체크아웃 시 **Hard Delete** |
| **request** | 고객 요청 (핵심 테이블) | 영구 보존 |
| **message** | AI 대화 메시지 | 영구 보존 (원문 증거) |
| **knowledge_entry** | RAG 지식 DB | 영구 (승인된 것만 검색 대상) |
| **unanswered_question** | 미답변 질문 (플라이휠 소스) | 승인 후 knowledge_entry로 승격 |
| **handover_briefing** | 교대 인수인계 브리핑 (AI 자동 생성) | 영구 보존 |
| **dispatch_log** | 실시간 알림 발송 이력 | 영구 (감사 로그) |

## 핵심 관계 설명

1. **guest ↔ room**: 1:1 (QR 토큰 ↔ 객실 매핑, 체크아웃 시 Hard Delete)
2. **room → request**: 1:N (한 객실에서 여러 요청 발생)
3. **room → message**: 1:N (한 객실에서 여러 대화 발생)
4. **request → department**: N:1 (요청은 하나의 부서로 라우팅)
5. **request → staff**: N:1 (요청은 한 직원에게 배정, nullable)
6. **request → message**: 1:N (요청과 관련된 대화)

## 인수인계 브리핑 자동 생성 로직

```
교대 버튼 클릭
    │
    ▼
[백엔드] shift_start ~ shift_end 기간의 request 조회
    ├── ① 미완료 태스크 (status != COMPLETED)
    ├── ② 에스컬레이션 거친 특이사항 (confidence < 0.7 또는 ESCALATED)
    └── ③ 긴급/주요 완료 건 (priority = URGENT, HIGH)
    │
    ▼ (JSON으로 AI 서버에 전달)
[AI] 3~5줄 자연어 브리핑 자동 생성
    │
    ▼
[handover_briefing] 저장 (통계 + AI 요약)
```

> **핵심**: 직원이 인수인계 내용을 직접 작성하지 않음.
> request + message 데이터가 자동으로 취합되어 AI가 요약합니다.





