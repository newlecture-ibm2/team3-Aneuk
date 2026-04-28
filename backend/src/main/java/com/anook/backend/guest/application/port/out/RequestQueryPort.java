package com.anook.backend.guest.application.port.out;

import java.util.List;

/**
 * Request 모듈 조회용 포트 — guest 모듈에서 request 데이터 접근 시 사용
 *
 * ❌ guest 모듈에서 request 모듈의 JPA Repository를 직접 import 금지
 * ✅ 이 포트를 통해서만 접근
 */
public interface RequestQueryPort {

    /**
     * 해당 객실에 미정산 F&B 요청이 존재하는지 확인
     *
     * @param roomId        객실 ID
     * @param departmentId  부서 코드 (예: "FB")
     * @param excludeStatuses 제외할 상태 목록 (예: ["SETTLED", "CANCELLED"])
     * @return 미정산 건 존재 여부
     */
    boolean existsByRoomIdAndDepartmentIdAndStatusNotIn(
            Long roomId, String departmentId, List<String> excludeStatuses);
}
