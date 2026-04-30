package com.anook.backend.staff.request.application.service;

import com.anook.backend.staff.request.adapter.in.web.dto.response.StaffTaskResult;
import com.anook.backend.staff.request.application.port.in.GetStaffRequestsUseCase;
import com.anook.backend.staff.request.application.port.out.RequestQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetStaffRequestsService implements GetStaffRequestsUseCase {

    private final RequestQueryPort requestQueryPort;

    @Override
    public List<StaffTaskResult> getRequests(String departmentId, String status, String priority) {
        return requestQueryPort.findRequests(departmentId, status, priority);
    }
}
