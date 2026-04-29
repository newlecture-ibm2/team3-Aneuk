-- ============================================================
-- 아늑(Aneuk) 초기 데이터
-- ============================================================

-- 부서 (6개 — 텍스트 PK: AI 파이프코드 연동)
INSERT INTO department (id, name) VALUES
    ('HK',        '하우스키핑'),
    ('FB',        '식음료'),
    ('FACILITY',  '시설관리'),
    ('CONCIERGE', '컨시어지'),
    ('FRONT',     '프론트데스크'),
    ('EMERGENCY', '긴급대응')
ON CONFLICT (id) DO NOTHING;

-- 직원 역할 (2개)
INSERT INTO staff_role (id, name) VALUES
    (1, '직원'),
    (2, '관리자')
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- ANOOK 객실 (호실 번호만 — PMS에서 수신한 목록)
-- ============================================================
INSERT INTO room (number) VALUES
    ('101'), ('102'), ('103'),
    ('201'), ('202'), ('203'),
    ('301'), ('302'), ('303'),
    ('707')
ON CONFLICT (number) DO NOTHING;

-- ============================================================
-- PMS 더미 데이터 (발표용 가짜 무대 세트)
-- ============================================================

-- PMS 객실
INSERT INTO pms_room (number, type) VALUES
    ('101', 'STANDARD'), ('102', 'STANDARD'), ('103', 'STANDARD'),
    ('201', 'DELUXE'),   ('202', 'DELUXE'),   ('203', 'DELUXE'),
    ('301', 'SUITE'),    ('302', 'SUITE'),     ('303', 'SUITE'),
    ('707', 'SUITE')
ON CONFLICT (number) DO NOTHING;
