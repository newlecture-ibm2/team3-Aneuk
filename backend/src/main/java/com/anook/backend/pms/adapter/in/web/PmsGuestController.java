package com.anook.backend.pms.adapter.in.web;

import com.anook.backend.guest.application.dto.request.CheckInGuestCommand;
import com.anook.backend.guest.application.dto.response.CheckInGuestResult;
import com.anook.backend.guest.application.dto.response.GetGuestResult;
import com.anook.backend.guest.application.port.in.CheckInGuestUseCase;
import com.anook.backend.guest.application.port.in.CheckOutGuestUseCase;
import com.anook.backend.guest.application.port.in.GetGuestUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PMS (가상 호텔 운영 시스템) — 투숙객 관리 Controller
 *
 * 발표 및 테스트용 더미 데이터 관리 전용.
 * guest 모듈의 UseCase를 호출합니다.
 */
@RestController
@RequestMapping("/pms/guests")
@RequiredArgsConstructor
public class PmsGuestController {

    private final CheckInGuestUseCase checkInGuestUseCase;
    private final CheckOutGuestUseCase checkOutGuestUseCase;
    private final GetGuestUseCase getGuestUseCase;

    /** 투숙객 전체 목록 조회 */
    @GetMapping
    public ResponseEntity<List<GetGuestResult>> getAll() {
        return ResponseEntity.ok(getGuestUseCase.getAll());
    }

    /** 체크인 (투숙객 추가) */
    @PostMapping
    public ResponseEntity<CheckInGuestResult> checkIn(@Valid @RequestBody CheckInGuestCommand command) {
        return ResponseEntity.ok(checkInGuestUseCase.checkIn(command));
    }

    /** 체크아웃 (Hard Delete) */
    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> checkOut(@PathVariable Long guestId) {
        checkOutGuestUseCase.checkOut(guestId);
        return ResponseEntity.noContent().build();
    }
}
