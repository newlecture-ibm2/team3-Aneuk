package com.anook.backend.admin.role.application.port.in;

import com.anook.backend.admin.role.application.dto.request.CreateRoleCommand;
import com.anook.backend.admin.role.application.dto.request.UpdateRoleCommand;
import com.anook.backend.admin.role.application.dto.response.GetRoleResult;

import java.util.List;

public interface ManageRoleUseCase {

    GetRoleResult create(CreateRoleCommand command);

    List<GetRoleResult> getAll();

    GetRoleResult getById(Long id);

    GetRoleResult update(Long id, UpdateRoleCommand command);

    void delete(Long id);
}
