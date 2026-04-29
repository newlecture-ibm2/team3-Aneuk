package com.anook.backend.guest.application.port.out;

import java.util.List;

/**
 * Request 모듈 조회용 포트 — pms_guest 체크아웃 시 미정산 건 확인
 */
public interface RequestQueryPort {

    /**
     * 해당 객실에 미정산 F&B 요청이 존재하는지 확인
     *
     * @param roomNumber     객실 번호
     * @param departmentId   부서 코드 (예: "FB")
     * @param excludeStatuses 제외할 상태 목록 (예: ["SETTLED", "CANCELLED"])
     * @return 미정산 건 존재 여부
     */
    boolean existsByRoomNumberAndDepartmentIdAndStatusNotIn(
            String roomNumber, String departmentId, List<String> excludeStatuses);
}
