-- ============================================================
-- 아늑(Aneuk) 초기 데이터 — 코드/룩업 테이블
-- ============================================================

-- 부서 (5개 — 텍스트 PK 유지: AI 파이프코드 연동)
INSERT INTO department (id, name, is_admin) VALUES
    ('HK',        '하우스키핑',   FALSE),
    ('FB',        '식음료',       FALSE),
    ('FACILITY',  '시설관리',     FALSE),
    ('CONCIERGE', '컨시어지',     FALSE),
    ('FRONT',     '프론트데스크', TRUE)
ON CONFLICT (id) DO NOTHING;

-- 객실 타입 (3개 — 숫자 PK, 호텔별 커스텀 가능)
INSERT INTO room_type (id, name) VALUES
    (1, '스탠다드'),
    (2, '디럭스'),
    (3, '스위트')
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
SELECT setval('room_type_id_seq', (SELECT COALESCE(MAX(id), 1) FROM room_type));
SELECT setval('staff_role_id_seq', (SELECT COALESCE(MAX(id), 1) FROM staff_role));

-- ============================================================
-- Mock PMS 시드 데이터 — 객실
-- ============================================================

-- 707호 객실 (스위트 = type_id 3)
INSERT INTO room (number, type_id) VALUES
    ('707', 3)
ON CONFLICT (number) DO NOTHING;
