---
trigger: always_on
---


## 네이밍 규칙
- **UseCase:** `{동작}{도메인}UseCase` (SubmitRequestUseCase)
- **Service:** `{동작}{도메인}Service` (SubmitRequestService)
- **Port(Out):** `{도메인}RepositoryPort` (RequestRepositoryPort)
- **Controller:** `{역할}{도메인}Controller` (AdminRequestController, GuestChatController)
- **Adapter:** `{도메인}PersistenceAdapter` (RequestPersistenceAdapter)
- **Command:** `{동작}{도메인}Command` (SubmitRequestCommand)
- **Result:** `{동작}{도메인}Result` (GetRequestDetailResult)

## API 경로 규칙 (BFF 패턴 적용)
- 백엔드 Controller는 `@RequestMapping`에 `/api` 접두어를 절대 사용하지 않는다. (BFF가 `/api`를 제거하고 전달)
- 투숙객 API: `/auth/guest`, `/chat/**`
- 직원 API: `/auth/staff`, `/staff/requests/**`
- 관리자 API: `/auth/admin`, `/admin/requests/**`, `/admin/handover/**`
- 인증 방식: Guest(방번호→JWT), Staff(PIN→JWT), Admin(ID+PW→JWT)
- JWT는 HttpOnly Cookie로 전달

## AI 연동 규칙
- AI 호출은 반드시 Port(Out) 인터페이스를 통해 추상화
- AI Adapter는 `adapter/out/ai/` 에 위치 (Python AI 서비스를 HTTP로 호출)
- Spring Boot는 Gemini를 직접 호출하지 않음 — 모든 AI 로직은 Python 서비스에서 처리
- 각 모듈은 자체 AI Port를 정의 (다른 모듈의 AI Port import 금지 → 패키지 의존 방지)
- AI 출력 포맷: 파이프 코드 (`HK|NORM|REQ_ITEM|TOWEL|2`)
- PII 마스킹은 AI 호출 전 `global/util/PiiMaskingUtil`로 선처리

## 실시간 통신 (WebSocket) 규칙
- WebSocket(STOMP) 메시지 발송 로직은 Service에서 템플릿(SimpMessagingTemplate)을 직접 호출하지 않는다.
- 반드시 `application/port/out/DispatchPort` 인터페이스를 통해 추상화하고, `adapter/out/websocket/` 에서 구현체를 작성한다.
- 채널 네이밍 규칙: `/topic/room/{roomNo}`, `/topic/dept/{deptCode}`, `/topic/admin`

## 금지 패턴
- ❌ Service에서 JPA Repository 직접 import
- ❌ Port(Out)에서 JPA Entity 반환
- ❌ Domain 모델 없이 JPA Entity가 도메인 역할 겸임
- ❌ Controller에서 비즈니스 로직 처리
- ❌ `findAll().stream().filter()` 패턴 (DB 쿼리로 필터링할 것)
- ❌ `@RequestBody Map<String, Object>` (전용 Request DTO 사용)
- ❌ 비표준 폴더 구조 (반드시 `adapter/in/web/`, `adapter/out/persistence/`)
- ❌ 빈혈 도메인 (Domain에 행위 메서드 없이 getter/setter만 있는 패턴)
- ❌ AI Adapter를 `adapter/out/ai/` 외부에 배치 (Service, Controller 등에 AI 호출 로직 금지)
- ❌ Spring Boot에서 Gemini API 직접 호출 (반드시 Python AI 서비스를 경유)
- ❌ 다른 모듈의 AI Port를 import (각 모듈은 자체 AI Port 정의)

## 리팩토링 교훈 기반 예방 규칙

### 모듈 구조 경고 신호 (즉시 수정)
- ⚠️ `presentation/` 폴더가 보이면 → `adapter/in/web/`으로 즉시 이동
- ⚠️ Service 하나에 2개 이상 도메인 로직이 있으면 → 도메인별 Service 분리
- ⚠️ DTO가 `dto/` 단일 폴더에 혼재되면 → `dto/request/`, `dto/response/`로 분리
- ⚠️ Port(In) UseCase 인터페이스가 10개 이상이면 → 모듈 분할 검토
- ⚠️ Service 메서드가 50줄 이상이면 → private 메서드로 단계 분리
- ⚠️ `findAll().stream().filter()` 패턴 발견 시 → DB 쿼리로 교체

### 모듈 생성 체크리스트 (새 모듈 추가 시 반드시 확인)
1. □ `domain/model/`에 순수 POJO 도메인 모델 존재하는가?
2. □ `application/port/out/`에 RepositoryPort가 도메인 모델만 반환하는가?
3. □ `application/service/`에서 JPA Repository를 직접 import하지 않는가?
4. □ `adapter/in/web/`에 Controller가 UseCase 인터페이스만 의존하는가?
5. □ `adapter/out/persistence/`에 PersistenceAdapter가 Domain ↔ Entity 매핑하는가?
6. □ DTO가 `dto/request/`, `dto/response/`에 별도 파일로 정의되어 있는가?
