package com.anook.backend.pms.application.port.in;

import com.anook.backend.pms.application.dto.response.GetPmsMenuResult;

import java.util.List;

/**
 * PMS 메뉴 조회 UseCase (Port In)
 */
public interface GetPmsMenuUseCase {

    List<GetPmsMenuResult> getAllMenus();

    List<GetPmsMenuResult> getAvailableMenus();

    GetPmsMenuResult getMenuById(Long id);
}
