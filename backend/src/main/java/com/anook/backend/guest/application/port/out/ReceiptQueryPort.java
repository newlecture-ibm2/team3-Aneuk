package com.anook.backend.guest.application.port.out;

import java.util.List;

/**
 * PMS 영수증 조회 Port (Guest 모듈 소유)
 *
 * pms_receipt 테이블을 읽기 전용으로 조회한다.
 * Guest 모듈에서 체크아웃 시 미결제 영수증 확인에 사용.
 */
public interface ReceiptQueryPort {

    /** 해당 객실에 미결제(UNPAID) 영수증이 있는지 확인 */
    boolean hasUnpaidReceipts(String roomNo);

    /** 해당 객실의 미결제 영수증 목록 조회 (메뉴명, 수량, 금액) */
    List<UnpaidReceiptInfo> findUnpaidByRoomNo(String roomNo);

    /** 미결제 영수증 정보 */
    record UnpaidReceiptInfo(
            Long id,
            String menuName,
            int quantity,
            int totalPrice
    ) {}
}
