-- ============================================================
-- 아늑(Aneuk) 초기 데이터 — 코드/룩업 테이블
-- ============================================================

-- 부서 (6개)
INSERT INTO department (id, name) VALUES
    ('HK',        '하우스키핑'),
    ('FB',        '식음료'),
    ('FACILITY',  '시설관리'),
    ('CONCIERGE', '컨시어지'),
    ('FRONT',     '프론트데스크'),
    ('EMERGENCY', '긴급대응')
ON CONFLICT (id) DO NOTHING;

-- 객실 타입 (3개)
INSERT INTO room_type (id, name) VALUES
    ('STANDARD', '스탠다드'),
    ('DELUXE',   '디럭스'),
    ('SUITE',    '스위트')
ON CONFLICT (id) DO NOTHING;

-- 직원 역할 (2개)
INSERT INTO staff_role (id, name) VALUES
    ('STAFF',   '직원'),
    ('MANAGER', '관리자')
ON CONFLICT (id) DO NOTHING;
