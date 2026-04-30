package com.anook.backend.pms.application.port.out;

import com.anook.backend.pms.domain.model.PmsReceipt;

import java.util.List;
import java.util.Optional;

/**
 * PMS 영수증 Repository Port (Out)
 */
public interface PmsReceiptRepositoryPort {

    void save(String roomNo, Long menuId, int quantity, int totalPrice);

    List<PmsReceipt> findByRoomNo(String roomNo);

    List<PmsReceipt> findUnpaidByRoomNo(String roomNo);

    Optional<PmsReceipt> findById(Long id);

    boolean hasUnpaidReceipts(String roomNo);

    void updateStatusById(Long id, String status);

    void updateStatusByRoomNo(String roomNo, String fromStatus, String toStatus);
}
