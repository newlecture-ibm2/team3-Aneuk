package com.anook.backend.admin.staff.application.port.out;

/**
 * 부서 조회 포트 — staff 모듈에서 department 존재 여부 확인용
 *
 * 다른 모듈(department)의 테이블을 직접 접근하지 않고,
 * 자기 모듈의 Port를 정의하여 의존 방향을 유지합니다.
 */
public interface DepartmentQueryPort {

    boolean existsById(String departmentId);
}
