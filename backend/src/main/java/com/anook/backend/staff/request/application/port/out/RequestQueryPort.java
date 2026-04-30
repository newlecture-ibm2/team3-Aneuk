package com.anook.backend.staff.request.application.port.out;

import com.anook.backend.staff.request.adapter.in.web.dto.response.StaffTaskResult;
import java.util.List;

/**
 * request 모듈로부터 데이터를 조회하기 위한 Port.
 * (staff 모듈이 request 모듈의 도메인을 직접 import하지 않도록 분리)
 */
public interface RequestQueryPort {
    
    /**
     * 필터 조건에 따라 요청 목록을 조회하여 StaffTaskResult 리스트로 반환합니다.
     */
    List<StaffTaskResult> findRequests(String departmentId, String status, String priority);
}
