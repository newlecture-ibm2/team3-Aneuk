package com.anook.backend.guest.adapter.in.web;

import com.anook.backend.guest.application.dto.request.CheckInGuestCommand;
import com.anook.backend.guest.application.dto.response.CheckInGuestResult;
import com.anook.backend.guest.application.dto.response.GetGuestResult;
import com.anook.backend.guest.application.port.in.CheckInGuestUseCase;
import com.anook.backend.guest.application.port.in.CheckOutGuestUseCase;
import com.anook.backend.guest.application.port.in.GetGuestUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 가상 PMS — 투숙객 관리 Controller
 *
 * ❌ /api 접두어 없음 (BFF가 /api를 제거하고 전달)
 * ❌ 비즈니스 로직 처리 금지 (UseCase 인터페이스에 위임)
 */
@RestController
@RequestMapping("/admin/guests")
@RequiredArgsConstructor
public class AdminGuestController {

    private final CheckInGuestUseCase checkInUseCase;
    private final CheckOutGuestUseCase checkOutUseCase;
    private final GetGuestUseCase getGuestUseCase;

    /**
     * 체크인 — POST /admin/guests/check-in
     */
    @PostMapping("/check-in")
    public ResponseEntity<CheckInGuestResult> checkIn(
            @Valid @RequestBody CheckInGuestCommand command) {
        CheckInGuestResult result = checkInUseCase.checkIn(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * 체크아웃 (Hard Delete) — DELETE /admin/guests/{guestId}
     */
    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> checkOut(@PathVariable Long guestId) {
        checkOutUseCase.checkOut(guestId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 투숙객 목록 조회 — GET /admin/guests
     */
    @GetMapping
    public ResponseEntity<List<GetGuestResult>> getGuests() {
        return ResponseEntity.ok(getGuestUseCase.getAll());
    }
}
