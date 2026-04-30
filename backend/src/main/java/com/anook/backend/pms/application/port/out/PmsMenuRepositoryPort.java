package com.anook.backend.pms.application.port.out;

import com.anook.backend.pms.domain.model.PmsMenu;

import java.util.List;
import java.util.Optional;

/**
 * PMS 메뉴 조회 Port (Out)
 */
public interface PmsMenuRepositoryPort {

    List<PmsMenu> findAllAvailable();

    List<PmsMenu> findAll();

    Optional<PmsMenu> findById(Long id);
}
