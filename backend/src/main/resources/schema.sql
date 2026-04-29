-- ============================================================
-- 아늑(Aneuk) 호텔 AI 컨시어지 — DDL (PostgreSQL 16 + pgvector)
-- ============================================================

-- pgvector 확장 활성화
CREATE EXTENSION IF NOT EXISTS vector;

-- ============================================================
-- 1. 코드/룩업 테이블
-- ============================================================

-- 부서
CREATE TABLE IF NOT EXISTS department (
    id          VARCHAR(20)  PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL
);

-- 직원 역할
CREATE TABLE IF NOT EXISTS staff_role (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL
);

-- ============================================================
-- 2. ANOOK 핵심 엔티티 테이블
-- ============================================================

-- 객실 (ANOOK) — 호실 번호만 보유. PMS에서 목록 수신.
CREATE TABLE IF NOT EXISTS room (
    number      VARCHAR(10)  PRIMARY KEY
);

-- 직원
CREATE TABLE IF NOT EXISTS staff (
    id              BIGSERIAL    PRIMARY KEY,
    name            VARCHAR(50)  NOT NULL,
    pin             VARCHAR(10)  NOT NULL,
    role_id         BIGINT       NOT NULL REFERENCES staff_role(id),
    department_id   VARCHAR(20)  NOT NULL REFERENCES department(id)
);

-- ============================================================
-- 3. 요청/메시지 테이블
-- ============================================================

-- 고객 요청 (핵심 테이블)
-- status: PENDING → ASSIGNED → IN_PROGRESS → COMPLETED → SETTLED(FB 결제완료) / CANCELLED
CREATE TABLE IF NOT EXISTS request (
    id                  BIGSERIAL    PRIMARY KEY,
    status              VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    priority            VARCHAR(10)  NOT NULL DEFAULT 'NORMAL',
    department_id       VARCHAR(20)  NOT NULL REFERENCES department(id),
    entities            JSONB,
    raw_text            TEXT,
    summary             TEXT,
    confidence          REAL,
    room_number         VARCHAR(10)  NOT NULL REFERENCES room(number),
    assigned_staff_id   BIGINT       REFERENCES staff(id),
    version             INT          NOT NULL DEFAULT 0,
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- AI 대화 메시지
CREATE TABLE IF NOT EXISTS message (
    id                  BIGSERIAL    PRIMARY KEY,
    sender_type         VARCHAR(10)  NOT NULL,
    content             TEXT         NOT NULL,
    translated_content  TEXT,
    room_number         VARCHAR(10)  NOT NULL REFERENCES room(number),
    request_id          BIGINT       REFERENCES request(id),
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- ============================================================
-- 4. AI 지식/학습 테이블
-- ============================================================

-- RAG 지식 DB (벡터 검색)
CREATE TABLE IF NOT EXISTS knowledge_entry (
    id              BIGSERIAL    PRIMARY KEY,
    question        TEXT         NOT NULL,
    answer          TEXT         NOT NULL,
    embedding       vector(768),
    domain_code     VARCHAR(20),
    status          VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    approved_by     BIGINT       REFERENCES staff(id),
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- 미답변 질문 (플라이휠)
CREATE TABLE IF NOT EXISTS unanswered_question (
    id                  BIGSERIAL    PRIMARY KEY,
    question            TEXT         NOT NULL,
    domain_code         VARCHAR(20),
    cluster_id          VARCHAR(50),
    suggested_answer    TEXT,
    status              VARCHAR(20)  NOT NULL DEFAULT 'NEW',
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- AI 자가 튜닝용 Few-Shot 데이터
CREATE TABLE IF NOT EXISTS fewshot_example (
    id              BIGSERIAL    PRIMARY KEY,
    input_text      TEXT         NOT NULL,
    correct_output  JSONB        NOT NULL,
    domain_code     VARCHAR(20)  NOT NULL,
    corrected_by    BIGINT       REFERENCES staff(id),
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- ============================================================
-- 5. 운영/감사 테이블
-- ============================================================

-- 교대 인수인계 브리핑 (AI 자동 생성)
CREATE TABLE IF NOT EXISTS handover_briefing (
    id                      BIGSERIAL    PRIMARY KEY,
    shift_start             TIMESTAMP    NOT NULL,
    shift_end               TIMESTAMP    NOT NULL,
    total_request_count     INT          NOT NULL DEFAULT 0,
    pending_count           INT          NOT NULL DEFAULT 0,
    escalated_count         INT          NOT NULL DEFAULT 0,
    summary                 TEXT,
    created_at              TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- 실시간 알림 발송 이력 (감사 로그)
CREATE TABLE IF NOT EXISTS dispatch_log (
    id          BIGSERIAL    PRIMARY KEY,
    target      VARCHAR(100) NOT NULL,
    event_type  VARCHAR(30)  NOT NULL,
    payload     TEXT,
    sent_at     TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- ============================================================
-- 6. PMS 테이블 (발표용 더미 데이터 — ANOOK과 분리)
-- ============================================================

-- PMS 객실
CREATE TABLE IF NOT EXISTS pms_room (
    number      VARCHAR(10)  PRIMARY KEY,
    type        VARCHAR(20)  NOT NULL
);

-- PMS 투숙객 (개인정보 포함 — ANOOK 접근 불가)
CREATE TABLE IF NOT EXISTS pms_guest (
    id              BIGSERIAL       PRIMARY KEY,
    room_number     VARCHAR(10)     NOT NULL UNIQUE
                                    REFERENCES pms_room(number)
                                    ON DELETE CASCADE,
    name            VARCHAR(50)     NOT NULL,
    phone           VARCHAR(20),
    checkin_date    TIMESTAMP       NOT NULL DEFAULT NOW(),
    checkout_date   DATE            NOT NULL
);

-- ============================================================
-- 7. 인덱스
-- ============================================================

-- 요청 조회 성능
CREATE INDEX IF NOT EXISTS idx_request_status ON request(status);
CREATE INDEX IF NOT EXISTS idx_request_room_number ON request(room_number);
CREATE INDEX IF NOT EXISTS idx_request_department_id ON request(department_id);
CREATE INDEX IF NOT EXISTS idx_request_created_at ON request(created_at DESC);

-- 메시지 조회 성능
CREATE INDEX IF NOT EXISTS idx_message_room_number ON message(room_number);
CREATE INDEX IF NOT EXISTS idx_message_request_id ON message(request_id);

-- 지식 도메인별 필터링
CREATE INDEX IF NOT EXISTS idx_knowledge_domain ON knowledge_entry(domain_code);
CREATE INDEX IF NOT EXISTS idx_knowledge_status ON knowledge_entry(status);

-- 미답변 질문 상태별 조회
CREATE INDEX IF NOT EXISTS idx_unanswered_status ON unanswered_question(status);

-- 디스패치 로그 시간순 조회
CREATE INDEX IF NOT EXISTS idx_dispatch_sent_at ON dispatch_log(sent_at DESC);
