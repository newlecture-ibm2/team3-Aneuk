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

-- ============================================================
-- 더미 데이터: 요청 (Request)
-- ============================================================

INSERT INTO request (id, department_id, summary, priority, status, room_no, created_at, updated_at) VALUES
    (1, 'HK', '수건 2장 요청', 'HIGH', 'PENDING', '101', NOW(), NOW()),
    (2, 'FRONT', '체크아웃 연장 문의', 'NORMAL', 'ASSIGNED', '205', NOW(), NOW()),
    (3, 'FACILITY', '에어컨 고장', 'URGENT', 'IN_PROGRESS', '302', NOW(), NOW()),
    (4, 'FB', '룸서비스: 조식 추가', 'NORMAL', 'PENDING', '501', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- 시퀀스 동기화
SELECT setval('request_id_seq', (SELECT COALESCE(MAX(id), 1) FROM request));

