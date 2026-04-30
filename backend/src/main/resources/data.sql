-- ============================================================
-- 아늑(Aneuk) 초기 데이터
-- ============================================================

-- 부서 (5개 — 텍스트 PK 유지: AI 파이프코드 연동)
INSERT INTO department (id, name, is_admin) VALUES
    ('HK',        '하우스키핑',   FALSE),
    ('FB',        '식음료',       FALSE),
    ('FACILITY',  '시설관리',     FALSE),
    ('CONCIERGE', '컨시어지',     FALSE),
    ('FRONT',     '프론트데스크', TRUE)
ON CONFLICT (id) DO NOTHING;

-- 직원 역할 (부서별 직급 추가)
INSERT INTO staff_role (id, department_id, name) VALUES
    (1, 'FRONT', '직원'),
    (2, 'FRONT', '관리자'),
    (3, 'FACILITY', '팀장'),
    (4, 'HK', '매니저'),
    (5, 'FB', '메인 셰프'),
    (6, 'CONCIERGE', '시니어'),
    (7, 'FACILITY', '엔지니어'),
    (8, 'HK', '현장 스태프'),
    (9, 'FB', '캡틴'),
    (10, 'CONCIERGE', '컨시어지')
ON CONFLICT (id) DO NOTHING;

-- 초기 관리자 계정 (PIN: 000000)
INSERT INTO staff (name, pin, role_id, department_id) VALUES
    ('관리자', '000000', 2, 'FRONT')
ON CONFLICT (pin) DO NOTHING;

-- 시퀀스 동기화 (수동 INSERT로 인해 시퀀스가 1로 남아있는 문제 해결)
SELECT setval('staff_role_id_seq', (SELECT COALESCE(MAX(id), 1) FROM staff_role));

-- ============================================================
-- ANOOK 객실 (호실 번호만 — PMS에서 수신한 목록)
-- ============================================================
INSERT INTO room (number) VALUES
    ('101'), ('102'), ('103'), ('104'), ('105'), ('106'),
    ('201'), ('202'), ('203'), ('204'), ('205'),
    ('301'), ('302'), ('303'), ('304'), ('305'),
    ('401'), ('402'), ('403'),
    ('501'), ('502'), ('503'),
    ('707')
ON CONFLICT (number) DO NOTHING;

-- ============================================================
-- PMS 더미 데이터 (발표용 가짜 무대 세트)
-- ============================================================

-- PMS 객실 (6개 타입 · 총 23실)
INSERT INTO pms_room (number, type) VALUES
    -- 1층: 스탠다드 (기본 객실)
    ('101', 'STANDARD'), ('102', 'STANDARD'), ('103', 'STANDARD'),
    ('104', 'STANDARD'), ('105', 'STANDARD'), ('106', 'STANDARD'),
    -- 2층: 슈페리어 (전망 좋은 객실)
    ('201', 'SUPERIOR'), ('202', 'SUPERIOR'), ('203', 'SUPERIOR'),
    ('204', 'SUPERIOR'), ('205', 'SUPERIOR'),
    -- 3층: 디럭스 (넓은 고급 객실)
    ('301', 'DELUXE'),   ('302', 'DELUXE'),   ('303', 'DELUXE'),
    ('304', 'DELUXE'),   ('305', 'DELUXE'),
    -- 4층: 패밀리 (가족용 넓은 객실)
    ('401', 'FAMILY'),   ('402', 'FAMILY'),   ('403', 'FAMILY'),
    -- 5층: 스위트 (거실+침실 분리)
    ('501', 'SUITE'),    ('502', 'SUITE'),    ('503', 'SUITE'),
    -- 7층: 프레지덴셜 (VIP 최상위)
    ('707', 'PRESIDENTIAL')
ON CONFLICT (number) DO NOTHING;

-- 테스트용 직원 1명 (직원 ID 1)
INSERT INTO staff (id, name, pin, role_id, department_id) VALUES
    (1, '김아늑', '1234', 1, 'HK')
ON CONFLICT (id) DO NOTHING;

-- 기존 요청 데이터 초기화 (ERD 기준: id 자동 생성 유지)
TRUNCATE TABLE request RESTART IDENTITY CASCADE;

INSERT INTO request (status, priority, department_id, summary, raw_text, confidence, room_no, assigned_staff_id, version, created_at, updated_at) VALUES
    ('PENDING',     'NORMAL', 'HK',        '수건 2장 추가 요청',      '수건 두 장만 더 주세요',              0.95, '707', NULL, 0, NOW() - INTERVAL '2 hours',      NOW() - INTERVAL '2 hours'),
    ('ASSIGNED',    'HIGH',   'FB',        '룸서비스 스테이크 주문',   '스테이크 미디엄으로 하나 주문할게요',   0.88, '707', 1,    0, NOW() - INTERVAL '1 hour',       NOW() - INTERVAL '30 minutes'),
    ('IN_PROGRESS', 'URGENT', 'FACILITY',  '에어컨 고장 수리 요청',    '에어컨이 안 켜져요',                 0.92, '707', 1,    0, NOW() - INTERVAL '45 minutes',   NOW() - INTERVAL '10 minutes'),
    ('COMPLETED',   'NORMAL', 'CONCIERGE', '택시 호출 요청',          '공항까지 택시 하나 불러주세요',        0.97, '707', 1,    0, NOW() - INTERVAL '3 hours',      NOW() - INTERVAL '1 hour'),
    ('PENDING',     'LOW',    'HK',        '미니바 보충 요청',        '미니바에 물이 없어요',                0.91, '707', NULL, 0, NOW() - INTERVAL '15 minutes',   NOW() - INTERVAL '15 minutes')
ON CONFLICT DO NOTHING;
