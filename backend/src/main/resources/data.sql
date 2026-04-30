-- ============================================================
-- 아늑(Aneuk) 초기 데이터 — 코드/룩업 테이블
-- ============================================================

-- 부서 (isAdmin 값 설정)
INSERT INTO department (id, name, is_admin) VALUES
    ('HK',        '하우스키핑',   FALSE),
    ('FB',        '식음료',       FALSE),
    ('FACILITY',  '시설관리',     FALSE),
    ('CONCIERGE', '컨시어지',     FALSE),
    ('FRONT',     '프론트데스크', TRUE), -- 프론트데스크는 관리자 권한
    ('EMERGENCY', '긴급대응',     TRUE)  -- 긴급대응도 관리자 권한
ON CONFLICT (id) DO UPDATE SET is_admin = EXCLUDED.is_admin;

-- 객실 타입
INSERT INTO room_type (id, name) VALUES
    (1, '스탠다드'),
    (2, '디럭스'),
    (3, '스위트')
ON CONFLICT (id) DO NOTHING;

-- 직원 역할
INSERT INTO staff_role (id, name) VALUES
    (1, '직원'),
    (2, '관리자')
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- Mock PMS 시드 데이터 — 객실
-- ============================================================

INSERT INTO room (number, type_id) VALUES
    ('707', 3)
ON CONFLICT (number) DO NOTHING;

-- ============================================================
-- ★ 테스트용 직원 데이터 (PIN 6자리 변경) ★
-- ============================================================

-- 관리자 계정 (PIN: 000000)
INSERT INTO staff (name, pin, role_id, department_id) VALUES
    ('최관리', '000000', 2, 'FRONT')
ON CONFLICT (pin) DO NOTHING;

-- 일반 직원 계정 (PIN: 111111)
INSERT INTO staff (name, pin, role_id, department_id) VALUES
    ('김직원', '111111', 1, 'HK')
ON CONFLICT (pin) DO NOTHING;
