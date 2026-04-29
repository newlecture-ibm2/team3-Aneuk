package com.anook.backend.admin.department.adapter.in.web;

import com.anook.backend.admin.department.application.dto.request.CreateDepartmentCommand;
import com.anook.backend.admin.department.application.dto.request.UpdateDepartmentCommand;
import com.anook.backend.admin.department.application.dto.response.GetDepartmentResult;
import com.anook.backend.admin.department.application.port.in.ManageDepartmentUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 부서 관리 Controller
 *
 * UseCase 인터페이스에만 의존 — 비즈니스 로직 없음
 */
@RestController
@RequestMapping("/admin/departments")
@RequiredArgsConstructor
public class AdminDepartmentController {

    private final ManageDepartmentUseCase manageDepartmentUseCase;

    @PostMapping
    public ResponseEntity<GetDepartmentResult> create(@Valid @RequestBody CreateDepartmentCommand command) {
        GetDepartmentResult result = manageDepartmentUseCase.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    public ResponseEntity<List<GetDepartmentResult>> getAll() {
        return ResponseEntity.ok(manageDepartmentUseCase.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetDepartmentResult> getById(@PathVariable String id) {
        return ResponseEntity.ok(manageDepartmentUseCase.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetDepartmentResult> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateDepartmentCommand command) {
        return ResponseEntity.ok(manageDepartmentUseCase.update(id, command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        manageDepartmentUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
