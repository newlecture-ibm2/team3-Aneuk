package com.anook.backend.admin.department.application.port.in;

import com.anook.backend.admin.department.application.dto.request.CreateDepartmentCommand;
import com.anook.backend.admin.department.application.dto.request.UpdateDepartmentCommand;
import com.anook.backend.admin.department.application.dto.response.GetDepartmentResult;

import java.util.List;

/**
 * 부서 관리 UseCase — Admin 전용 CRUD
 */
public interface ManageDepartmentUseCase {

    GetDepartmentResult create(CreateDepartmentCommand command);

    List<GetDepartmentResult> getAll();

    GetDepartmentResult getById(String id);

    GetDepartmentResult update(String id, UpdateDepartmentCommand command);

    void delete(String id);
}
