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

-- 추가 객실
INSERT INTO room (number, type_id) VALUES
    ('501', 1),
    ('502', 2),
    ('1204', 1),
    ('1502', 3)
ON CONFLICT (number) DO NOTHING;

-- ============================================================
-- Mock 시드 데이터 — 직원
-- ============================================================

INSERT INTO staff (name, pin, role_id, department_id)
SELECT '김하우스', '1234', 1, 'HK'
WHERE NOT EXISTS (SELECT 1 FROM staff WHERE name = '김하우스');

INSERT INTO staff (name, pin, role_id, department_id)
SELECT '박시설', '5678', 1, 'FACILITY'
WHERE NOT EXISTS (SELECT 1 FROM staff WHERE name = '박시설');

-- ============================================================
-- Mock 시드 데이터 — 요청 (대시보드 테스트용)
-- ============================================================

-- 대기 중 (PENDING) 요청들
INSERT INTO request (status, priority, department_id, raw_text, summary, confidence, room_no, version, created_at, updated_at)
SELECT 'PENDING', 'NORMAL', 'HK', '새 수건 3장 추가 요청', '수건 교체', 0.95,
       '1204', 0, NOW() - INTERVAL '30 minutes', NOW() - INTERVAL '30 minutes'
WHERE NOT EXISTS (SELECT 1 FROM request WHERE summary = '수건 교체' AND status = 'PENDING');

INSERT INTO request (status, priority, department_id, raw_text, summary, confidence, room_no, version, created_at, updated_at)
SELECT 'PENDING', 'HIGH', 'HK', '객실 청소 요청드립니다. 외출 후 돌아왔는데 아직 정리가 안 되어있어요.', '객실 청소 요청', 0.92,
       '501', 0, NOW() - INTERVAL '15 minutes', NOW() - INTERVAL '15 minutes'
WHERE NOT EXISTS (SELECT 1 FROM request WHERE summary = '객실 청소 요청' AND status = 'PENDING');

-- 진행 중 (IN_PROGRESS) 요청
INSERT INTO request (status, priority, department_id, raw_text, summary, confidence, room_no, assigned_staff_id, version, created_at, updated_at)
SELECT 'IN_PROGRESS', 'HIGH', 'HK', '엑스트라 베드 설치 요청', '침구류 정리', 0.88,
       '502',
       (SELECT id FROM staff WHERE name = '김하우스'),
       1, NOW() - INTERVAL '1 hour', NOW() - INTERVAL '20 minutes'
WHERE NOT EXISTS (SELECT 1 FROM request WHERE summary = '침구류 정리' AND status = 'IN_PROGRESS');

-- 완료됨 (COMPLETED) 요청
INSERT INTO request (status, priority, department_id, raw_text, summary, confidence, room_no, assigned_staff_id, version, created_at, updated_at)
SELECT 'COMPLETED', 'NORMAL', 'HK', '미니바 음료 보충해주세요', '미니바 보충', 0.97,
       '707',
       (SELECT id FROM staff WHERE name = '김하우스'),
       2, NOW() - INTERVAL '3 hours', NOW() - INTERVAL '1 hour'
WHERE NOT EXISTS (SELECT 1 FROM request WHERE summary = '미니바 보충' AND status = 'COMPLETED');

-- 시설 관련 요청 (다른 부서)
INSERT INTO request (status, priority, department_id, raw_text, summary, confidence, room_no, version, created_at, updated_at)
SELECT 'PENDING', 'URGENT', 'FACILITY', '에어컨이 작동하지 않습니다. 너무 더워요.', '에어컨 고장', 0.90,
       '1502', 0, NOW() - INTERVAL '10 minutes', NOW() - INTERVAL '10 minutes'
WHERE NOT EXISTS (SELECT 1 FROM request WHERE summary = '에어컨 고장' AND status = 'PENDING');
