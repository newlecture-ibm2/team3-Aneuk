package com.anook.backend.pms.adapter.in.web;

import com.anook.backend.pms.application.dto.response.GetPmsMenuResult;
import com.anook.backend.pms.application.port.in.GetPmsMenuUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PMS 메뉴 Controller
 *
 * GET /pms/menus           전체 메뉴 목록 (available 필터 옵션)
 * GET /pms/menus/{id}      메뉴 상세 조회
 */
@RestController
@RequestMapping("/pms/menus")
@RequiredArgsConstructor
public class PmsMenuController {

    private final GetPmsMenuUseCase getPmsMenuUseCase;

    @GetMapping
    public ResponseEntity<List<GetPmsMenuResult>> getMenus(
            @RequestParam(name = "available", required = false) Boolean available) {

        List<GetPmsMenuResult> menus;
        if (Boolean.TRUE.equals(available)) {
            menus = getPmsMenuUseCase.getAvailableMenus();
        } else {
            menus = getPmsMenuUseCase.getAllMenus();
        }
        return ResponseEntity.ok(menus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPmsMenuResult> getMenu(@PathVariable Long id) {
        return ResponseEntity.ok(getPmsMenuUseCase.getMenuById(id));
    }
}
