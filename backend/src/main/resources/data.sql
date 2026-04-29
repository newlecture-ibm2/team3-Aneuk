-- ============================================================
-- 아늑(Aneuk) 초기 데이터 — 코드/룩업 테이블
-- ============================================================

-- 부서 (6개 — 텍스트 PK 유지: AI 파이프코드 연동)
INSERT INTO department (id, name, is_admin) VALUES
    ('HK',        '하우스키핑',   FALSE),
    ('FB',        '식음료',       FALSE),
    ('FACILITY',  '시설관리',     FALSE),
    ('CONCIERGE', '컨시어지',     FALSE),
    ('FRONT',     '프론트데스크', TRUE),
    ('EMERGENCY', '긴급대응',     FALSE)
ON CONFLICT (id) DO NOTHING;

-- 객실 타입 (3개 — 숫자 PK, 호텔별 커스텀 가능)
INSERT INTO room_type (id, name) VALUES
    (1, '스탠다드'),
    (2, '디럭스'),
    (3, '스위트')
ON CONFLICT (id) DO NOTHING;

-- 직원 역할 (2개 — 숫자 PK, 호텔별 커스텀 가능)
INSERT INTO staff_role (id, name) VALUES
    (1, '직원'),
    (2, '관리자')
ON CONFLICT (id) DO NOTHING;

-- 초기 관리자 계정 (PIN: 0000)
INSERT INTO staff (name, pin, role_id, department_id) VALUES
    ('관리자', '0000', 1, 'FRONT')
ON CONFLICT (pin) DO NOTHING;

-- ============================================================
-- Mock PMS 시드 데이터 — 객실
-- ============================================================

-- 707호 객실 (스위트 = type_id 3)
INSERT INTO room (number, type_id) VALUES
    ('707', 3)
ON CONFLICT (number) DO NOTHING;
