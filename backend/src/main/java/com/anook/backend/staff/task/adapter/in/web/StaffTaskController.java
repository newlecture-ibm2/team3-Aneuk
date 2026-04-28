package com.anook.backend.staff.task.adapter.in.web;

import com.anook.backend.staff.task.application.port.in.GetStaffTasksUseCase;
import com.anook.backend.staff.task.application.result.GetStaffTasksResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff/requests")
@RequiredArgsConstructor
public class StaffTaskController {

    private final GetStaffTasksUseCase getStaffTasksUseCase;

    @GetMapping
    public List<GetStaffTasksResult> getTasks(
            @RequestParam(value = "status", defaultValue = "ALL") String status,
            @RequestParam(value = "priority", defaultValue = "ALL") String priority,
            @RequestParam(value = "departmentId", defaultValue = "ALL") String departmentId) {
        return getStaffTasksUseCase.getTasks(status, priority, departmentId);
    }
}
