package com.anook.backend.request.adapter.in.web;

import com.anook.backend.request.application.dto.response.GetMyRequestsResult;
import com.anook.backend.request.application.port.in.GetMyRequestsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 투숙객 전용 요청 조회 컨트롤러
 */
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class GuestRequestController {

    private final GetMyRequestsUseCase getMyRequestsUseCase;

    @GetMapping("/{roomNo}/requests")
    public ResponseEntity<List<GetMyRequestsResult>> getMyRequests(@PathVariable String roomNo) {
        List<GetMyRequestsResult> results = getMyRequestsUseCase.getMyRequests(roomNo);
        return ResponseEntity.ok(results);
    }
}
