package com.anook.backend.request.application.port.in;

import com.anook.backend.request.application.dto.response.GetMyRequestsResult;

import java.util.List;

/**
 * 고객 요청 상태 조회 UseCase
 */
public interface GetMyRequestsUseCase {
    
    /**
     * 방 번호로 모든 요청 목록을 조회합니다.
     * 
     * @param roomNo 방 번호
     * @return 요청 목록
     */
    List<GetMyRequestsResult> getMyRequests(String roomNo);
}
