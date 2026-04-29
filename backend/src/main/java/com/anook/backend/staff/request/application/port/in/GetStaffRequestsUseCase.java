package com.anook.backend.staff.request.application.port.in;

import com.anook.backend.staff.request.application.dto.response.GetStaffRequestsResult;
import java.util.List;

public interface GetStaffRequestsUseCase {
    List<GetStaffRequestsResult> getRequests(String status, String priority, String departmentId);
    GetStaffRequestsResult getRequestDetail(Long id);
}
