# 아늑(Aneuk) REST API 명세서

> **v1.0** — 아키텍처 구조 제안서 + ERD v2.0 기반
> 인증: JWT (HttpOnly Cookie), BFF 패턴

### ⚠️ 경로 규칙 (BFF 패턴)
> 이 문서의 모든 URL은 **Spring Boot Controller 기준 (백엔드 경로)** 입니다.
>
> | 계층 | 경로 | 예시 |
> |------|------|------|
> | **프론트 코드** (브라우저 → BFF) | `/api` 접두어 **있음** | `fetch('/api/auth/guest')` |
> | **백엔드 Controller** (이 문서) | `/api` 접두어 **없음** | `@PostMapping("/auth/guest")` |
> | **BFF 프록시** | `/api` 제거 후 `app:8080`으로 전달 | `/api/auth/guest` → `http://app:8080/auth/guest` |

---

## 1. 인증 (Auth)

### 1.1 투숙객 인증 (정적 QR)
| 항목 | 값 |
|------|-----|
| **Method** | `POST` |
| **URL** | `/auth/guest` |
| **설명** | 정적 QR 스캔 → 방번호로 활성 GuestSession 존재 확인 → JWT 발급 |

**Request Body**
```json
{ "roomNumber": "302" }
```

**Response 200**
```json
{ "guestId": 1, "roomNumber": "302", "language": "en" }
```
> JWT는 `Set-Cookie: token=...; HttpOnly; Secure` 헤더로 전달

**Response 401** — 빈 방 또는 체크아웃 완료
```json
{ "code": "GUEST_NOT_FOUND", "message": "해당 객실에 체크인된 투숙객이 없습니다." }
```

---

### 1.2 직원 인증 (PIN)
| 항목 | 값 |
|------|-----|
| **Method** | `POST` |
| **URL** | `/auth/staff` |
| **설명** | 4자리 PIN 검증 → JWT 발급 (1코드 1기기 세션 제어) |

**Request Body**
```json
{ "pin": "1234" }
```

**Response 200**
```json
{ "staffId": 5, "name": "김철수", "role": "STAFF", "departmentCode": "HK" }
```

**Response 401** — PIN 불일치 / 비활성 계정
```json
{ "code": "INVALID_PIN", "message": "유효하지 않은 코드입니다." }
```

---

### 1.3 관리자 인증 (ID+PW)
| 항목 | 값 |
|------|-----|
| **Method** | `POST` |
| **URL** | `/auth/admin` |
| **설명** | ID/PW 검증 → JWT 발급 |

**Request Body**
```json
{ "username": "admin01", "password": "securePass!" }
```

**Response 200**
```json
{ "staffId": 1, "name": "관리자", "role": "MANAGER" }
```

---

## 2. 고객 대화 (Message)

### 2.1 메시지 전송
| 항목 | 값 |
|------|-----|
| **Method** | `POST` |
| **URL** | `/chat/{roomNo}/messages` |
| **Auth** | Guest JWT |
| **설명** | 고객 메시지 전송 → 즉시 202 응답 → AI 분석 비동기 처리 → WebSocket Push |

**Request Body**
```json
{ "content": "Can I get two extra towels?" }
```

**Response 202**
```json
{ "messageId": 42, "status": "PROCESSING" }
```

> AI 응답은 WebSocket `/topic/room/{roomNo}` 채널로 Push

---

### 2.2 대화 내역 조회
| 항목 | 값 |
|------|-----|
| **Method** | `GET` |
| **URL** | `/chat/{roomNo}/messages` |
| **Auth** | Guest JWT |
| **Query** | `?cursor={lastMessageId}&size=20` |

**Response 200**
```json
{
  "messages": [
    {
      "id": 41,
      "senderType": "GUEST",
      "content": "수건 2장 주세요",
      "translatedContent": null,
      "createdAt": "2026-05-10T14:30:00"
    },
    {
      "id": 42,
      "senderType": "AI",
      "content": "네, 수건 2장을 객실로 보내드리겠습니다.",
      "translatedContent": "Sure, we'll send 2 towels to your room.",
      "createdAt": "2026-05-10T14:30:03"
    }
  ],
  "hasMore": true,
  "nextCursor": 40
}
```

---

## 3. 고객 요청 (Request)

### 3.1 요청 접수 (AI 자동 태스크 생성)
| 항목 | 값 |
|------|-----|
| **Method** | `POST` |
| **URL** | `/chat/{roomNo}/requests` |
| **Auth** | Guest JWT |
| **설명** | 고객 요청 접수 → AI 파이프코드 분석 → 태스크 자동 생성 + 부서 배정 |

**Request Body**
```json
{ "rawText": "수건 2장이랑 물 좀 가져다 주세요" }
```

**Response 202**
```json
{
  "requestId": 100,
  "status": "PENDING",
  "message": "요청이 접수되었습니다."
}
```

---

### 3.2 본인 요청 상태 조회
| 항목 | 값 |
|------|-----|
| **Method** | `GET` |
| **URL** | `/chat/{roomNo}/requests` |
| **Auth** | Guest JWT |

**Response 200**
```json
{
  "requests": [
    {
      "id": 100,
      "status": "IN_PROGRESS",
      "priority": "NORMAL",
      "domainCode": "HK",
      "summary": "수건 2장 요청",
      "createdAt": "2026-05-10T14:30:00",
      "updatedAt": "2026-05-10T14:32:00"
    }
  ]
}
```

---

## 4. 직원 태스크 (Staff Task)

### 4.1 배정된 태스크 목록 조회
| 항목 | 값 |
|------|-----|
| **Method** | `GET` |
| **URL** | `/staff/tasks` |
| **Auth** | Staff JWT |
| **Query** | `?status=ASSIGNED,IN_PROGRESS&sort=createdAt,desc` |

**Response 200**
```json
{
  "tasks": [
    {
      "id": 100,
      "status": "ASSIGNED",
      "priority": "NORMAL",
      "domainCode": "HK",
      "summary": "수건 2장 요청",
      "roomNumber": "302",
      "entities": { "item": "수건", "quantity": 2 },
      "createdAt": "2026-05-10T14:30:00",
      "version": 1
    }
  ]
}
```

---

### 4.2 태스크 수락
| 항목 | 값 |
|------|-----|
| **Method** | `PATCH` |
| **URL** | `/staff/tasks/{taskId}/accept` |
| **Auth** | Staff JWT |
| **설명** | 낙관적 락(version) 적용. 동시 수락 시 먼저 요청한 직원만 성공 |

**Request Body**
```json
{ "version": 1 }
```

**Response 200**
```json
{ "id": 100, "status": "IN_PROGRESS", "assignedStaffId": 5, "version": 2 }
```

**Response 409** — 동시 수락 충돌
```json
{ "code": "OPTIMISTIC_LOCK_CONFLICT", "message": "이미 다른 직원이 수락했습니다." }
```

---

### 4.3 태스크 완료
| 항목 | 값 |
|------|-----|
| **Method** | `PATCH` |
| **URL** | `/staff/tasks/{taskId}/complete` |
| **Auth** | Staff JWT |

**Request Body**
```json
{ "note": "수건 2장 전달 완료", "version": 2 }
```

**Response 200**
```json
{ "id": 100, "status": "COMPLETED", "version": 3 }
```

---

### 4.4 태스크 부서 전달
| 항목 | 값 |
|------|-----|
| **Method** | `PATCH` |
| **URL** | `/staff/tasks/{taskId}/transfer` |
| **Auth** | Staff JWT |

**Request Body**
```json
{ "toDepartmentId": 3, "reason": "시설관리팀 소관 건", "version": 2 }
```

**Response 200**
```json
{ "id": 100, "status": "ASSIGNED", "departmentId": 3, "version": 3 }
```

---

### 4.5 투숙객 메시지 응답 (자동 번역)
| 항목 | 값 |
|------|-----|
| **Method** | `POST` |
| **URL** | `/staff/tasks/{taskId}/respond` |
| **Auth** | Staff JWT |
| **설명** | 직원이 한국어로 작성 → 투숙객 언어로 자동 번역하여 전달 |

**Request Body**
```json
{ "content": "수건 2장 지금 가져다 드리겠습니다." }
```

**Response 200**
```json
{
  "messageId": 55,
  "content": "수건 2장 지금 가져다 드리겠습니다.",
  "translatedContent": "I'll bring 2 towels to your room right now.",
  "senderType": "STAFF"
}
```

---

## 5. 관리자 — 태스크 모니터링 (Admin Task)

### 5.1 전체 요청 현황 조회
| 항목 | 값 |
|------|-----|
| **Method** | `GET` |
| **URL** | `/admin/tasks` |
| **Auth** | Admin JWT |
| **Query** | `?status=PENDING&department=HK&priority=URGENT&page=0&size=20` |

**Response 200**
```json
{
  "content": [
    {
      "id": 100,
      "status": "PENDING",
      "priority": "NORMAL",
      "domainCode": "HK",
      "summary": "수건 2장 요청",
      "roomNumber": "302",
      "assignedStaffName": null,
      "confidence": 0.95,
      "createdAt": "2026-05-10T14:30:00"
    }
  ],
  "totalElements": 45,
  "totalPages": 3,
  "currentPage": 0
}
```

---

### 5.2 요청 상세 조회
| 항목 | 값 |
|------|-----|
| **Method** | `GET` |
| **URL** | `/admin/tasks/{taskId}` |
| **Auth** | Admin JWT |

**Response 200**
```json
{
  "id": 100,
  "status": "PENDING",
  "priority": "NORMAL",
  "domainCode": "HK",
  "rawText": "수건 2장이랑 물 좀 가져다 주세요",
  "summary": "수건 2장 요청",
  "entities": { "item": "수건", "quantity": 2 },
  "confidence": 0.95,
  "roomNumber": "302",
  "assignedStaff": null,
  "departmentName": "하우스키핑",
  "messages": [],
  "version": 1,
  "createdAt": "2026-05-10T14:30:00"
}
```

---

### 5.3 에스컬레이션 컨펌 (승인)
| 항목 | 값 |
|------|-----|
| **Method** | `POST` |
| **URL** | `/admin/tasks/{taskId}/confirm` |
| **Auth** | Admin JWT |
| **설명** | AI 신뢰도 < 0.7 또는 ETC 코드로 에스컬레이션된 건을 관리자가 최종 승인 |

**Request Body**
```json
{
  "departmentId": 2,
  "priority": "NORMAL",
  "correctedDomainCode": "FB",
  "note": "식음료팀 소관으로 정정"
}
```

**Response 200**
```json
{ "id": 100, "status": "ASSIGNED", "departmentId": 2, "domainCode": "FB" }
```

---

### 5.4 요청 수동 재배정
| 항목 | 값 |
|------|-----|
| **Method** | `PATCH` |
| **URL** | `/admin/tasks/{taskId}/reassign` |
| **Auth** | Admin JWT |

**Request Body**
```json
{ "toDepartmentId": 4, "reason": "컨시어지 소관 건으로 재배정" }
```

**Response 200**
```json
{ "id": 100, "status": "ASSIGNED", "departmentId": 4 }
```

---

### 5.5 수동 태스크 생성
| 항목 | 값 |
|------|-----|
| **Method** | `POST` |
| **URL** | `/admin/tasks` |
| **Auth** | Admin JWT |

**Request Body**
```json
{
  "roomNumber": "302",
  "domainCode": "HK",
  "priority": "HIGH",
  "summary": "VIP 고객 특별 어메니티 세팅",
  "entities": { "item": "어메니티세트", "quantity": 1 }
}
```

**Response 201**
```json
{ "id": 150, "status": "PENDING", "domainCode": "HK" }
```

---

## 6. 관리자 — 설정 (Admin Settings)

### 6.1 직원 CRUD

| Method | URL | 설명 |
|--------|-----|------|
| `GET` | `/admin/staff` | 전체 직원 목록 조회 (`?department=HK`) |
| `POST` | `/admin/staff` | 직원 등록 (PIN 자동 발급) |
| `GET` | `/admin/staff/{id}` | 직원 상세 조회 |
| `PUT` | `/admin/staff/{id}` | 직원 정보 수정 |
| `DELETE` | `/admin/staff/{id}` | 직원 비활성화 |

**POST Request Body**
```json
{ "name": "김철수", "departmentId": 1, "role": "STAFF" }
```

**POST Response 201**
```json
{ "id": 5, "name": "김철수", "pin": "7291", "departmentCode": "HK", "role": "STAFF" }
```

---

### 6.2 객실 CRUD

| Method | URL | 설명 |
|--------|-----|------|
| `GET` | `/admin/rooms` | 전체 객실 목록 |
| `POST` | `/admin/rooms` | 객실 등록 |
| `PUT` | `/admin/rooms/{id}` | 객실 수정 |
| `DELETE` | `/admin/rooms/{id}` | 객실 삭제 |

**POST Request Body**
```json
{ "number": "501", "floor": 5, "type": "SUITE" }
```

---

### 6.3 부서 CRUD

| Method | URL | 설명 |
|--------|-----|------|
| `GET` | `/admin/departments` | 전체 부서 목록 |
| `POST` | `/admin/departments` | 부서 등록 |
| `PUT` | `/admin/departments/{id}` | 부서 수정 |
| `DELETE` | `/admin/departments/{id}` | 부서 삭제 |

**POST Request Body**
```json
{ "code": "SPA", "name": "스파/웰니스" }
```

---

## 7. 관리자 — 투숙객 관리 (가상 PMS)

### 7.1 체크인
| 항목 | 값 |
|------|-----|
| **Method** | `POST` |
| **URL** | `/admin/guests/check-in` |
| **Auth** | Admin JWT |

**Request Body**
```json
{ "roomId": 3, "language": "en" }
```

**Response 201**
```json
{ "guestId": 10, "roomNumber": "302", "language": "en", "createdAt": "2026-05-10T14:00:00" }
```

---

### 7.2 체크아웃 (Hard Delete)
| 항목 | 값 |
|------|-----|
| **Method** | `DELETE` |
| **URL** | `/admin/guests/{guestId}` |
| **Auth** | Admin JWT |

**Response 204** — No Content

---

### 7.3 투숙객 목록 조회
| 항목 | 값 |
|------|-----|
| **Method** | `GET` |
| **URL** | `/admin/guests` |
| **Auth** | Admin JWT |

**Response 200**
```json
{
  "guests": [
    { "id": 10, "roomNumber": "302", "language": "en", "createdAt": "2026-05-10T14:00:00" },
    { "id": 11, "roomNumber": "501", "language": "ja", "createdAt": "2026-05-10T15:00:00" }
  ]
}
```

---

## 8. 관리자 — 인수인계 브리핑 (Handover)

### 8.1 브리핑 생성 (AI 자동 요약)
| 항목 | 값 |
|------|-----|
| **Method** | `POST` |
| **URL** | `/admin/handover` |
| **Auth** | Admin JWT |
| **설명** | 교대 시간 기준 태스크 필터링 → AI 3~5줄 자연어 브리핑 생성 |

**Request Body**
```json
{ "shiftStart": "2026-05-10T08:00:00", "shiftEnd": "2026-05-10T16:00:00" }
```

**Response 201**
```json
{
  "id": 5,
  "shiftStart": "2026-05-10T08:00:00",
  "shiftEnd": "2026-05-10T16:00:00",
  "totalRequestCount": 23,
  "pendingCount": 3,
  "escalatedCount": 1,
  "summary": "금일 08~16시 교대 기간 총 23건 처리. 미완료 3건(302호 수건 추가 요청 포함). 501호 에어컨 고장 건은 시설관리팀 확인 중. VIP 801호 특별 어메니티 정상 완료.",
  "createdAt": "2026-05-10T16:05:00"
}
```

---

### 8.2 브리핑 목록 조회
| 항목 | 값 |
|------|-----|
| **Method** | `GET` |
| **URL** | `/admin/handover` |
| **Auth** | Admin JWT |
| **Query** | `?page=0&size=10` |

---

### 8.3 브리핑 상세 조회
| 항목 | 값 |
|------|-----|
| **Method** | `GET` |
| **URL** | `/admin/handover/{id}` |
| **Auth** | Admin JWT |

---

## 9. 지식 관리 (Knowledge)

### 9.1 FAQ 검색 (Guest)
| 항목 | 값 |
|------|-----|
| **Method** | `GET` |
| **URL** | `/chat/{roomNo}/knowledge` |
| **Auth** | Guest JWT |
| **Query** | `?q=조식 시간` |

**Response 200**
```json
{
  "results": [
    { "question": "조식 시간이 어떻게 되나요?", "answer": "조식은 매일 07:00~10:00...", "similarity": 0.92 }
  ]
}
```

---

### 9.2 승인 대기 지식 목록 (Admin)
| 항목 | 값 |
|------|-----|
| **Method** | `GET` |
| **URL** | `/admin/knowledge` |
| **Auth** | Admin JWT |
| **Query** | `?status=PENDING` |

---

### 9.3 지식 승인/반려 (Admin)
| 항목 | 값 |
|------|-----|
| **Method** | `PATCH` |
| **URL** | `/admin/knowledge/{id}/approve` |
| **Auth** | Admin JWT |

**Request Body**
```json
{ "action": "APPROVE" }
```

---

## 10. WebSocket 이벤트 채널

| 채널 | 구독 대상 | 이벤트 | Payload 예시 |
|------|-----------|--------|-------------|
| `/topic/room/{roomNo}` | Guest | AI 응답 도착 | `{ "type": "AI_RESPONSE", "message": {...} }` |
| `/topic/room/{roomNo}` | Guest | 요청 상태 변경 | `{ "type": "STATUS_CHANGED", "requestId": 100, "status": "IN_PROGRESS" }` |
| `/topic/dept/{deptCode}` | Staff | 새 태스크 배정 | `{ "type": "NEW_REQUEST", "task": {...} }` |
| `/topic/dept/{deptCode}` | Staff | 태스크 상태 변경 | `{ "type": "TASK_UPDATED", "taskId": 100, "status": "IN_PROGRESS" }` |
| `/topic/admin` | Admin | 에스컬레이션 알림 | `{ "type": "ESCALATED", "requestId": 100, "reason": "ETC 코드" }` |

---

## 11. 공통 에러 응답 형식

```json
{
  "code": "ERROR_CODE",
  "message": "사용자 친화적 에러 메시지",
  "timestamp": "2026-05-10T14:30:00"
}
```

| HTTP Status | 코드 | 설명 |
|-------------|------|------|
| 400 | `INVALID_REQUEST` | 요청 파라미터 오류 |
| 401 | `UNAUTHORIZED` | 인증 실패 / 토큰 만료 |
| 403 | `FORBIDDEN` | 권한 없음 |
| 404 | `NOT_FOUND` | 리소스 없음 |
| 409 | `OPTIMISTIC_LOCK_CONFLICT` | 동시 수정 충돌 (낙관적 락) |
| 500 | `INTERNAL_ERROR` | 서버 내부 오류 |

---

## 12. Spring Boot ↔ Python AI 내부 API

> 이 API는 내부망 전용이며 외부 노출되지 않습니다.

### 12.1 NLU 분석 요청
| 항목 | 값 |
|------|-----|
| **Method** | `POST` |
| **URL** | `http://ai:8000/analyze` |
| **호출자** | Spring Boot (`adapter/out/ai/`) |

**Request Body**
```json
{
  "text": "Can I get two extra towels?",
  "roomId": 3,
  "guestLanguage": "en",
  "fewShotExamples": [
    { "input": "종이컵 줘", "output": "HK|NORM|REQ_ITEM|ETC|1|\"종이컵\"" }
  ]
}
```

**Response 200**
```json
{
  "domainCode": "HK",
  "priority": "NORMAL",
  "entities": { "item": "TOWEL", "quantity": 2 },
  "pipeCode": "HK|NORM|REQ_ITEM|TOWEL|2",
  "confidence": 0.95,
  "guestReply": "네, 수건 2장을 객실로 보내드리겠습니다.",
  "guestReplyTranslated": "Sure, we'll send 2 towels to your room.",
  "clarificationNeeded": false
}
```

### 12.2 인수인계 요약 요청
| 항목 | 값 |
|------|-----|
| **Method** | `POST` |
| **URL** | `http://ai:8000/handover/summarize` |

### 12.3 RAG 검색 요청
| 항목 | 값 |
|------|-----|
| **Method** | `POST` |
| **URL** | `http://ai:8000/rag/search` |

### 12.4 번역 요청 (직원 응답)
| 항목 | 값 |
|------|-----|
| **Method** | `POST` |
| **URL** | `http://ai:8000/translate` |
