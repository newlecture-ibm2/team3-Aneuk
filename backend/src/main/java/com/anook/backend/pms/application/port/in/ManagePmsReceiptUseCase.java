package com.anook.backend.pms.application.port.in;

import com.anook.backend.pms.application.dto.request.CreateReceiptCommand;
import com.anook.backend.pms.application.dto.response.GetPmsReceiptResult;

import java.util.List;

/**
 * PMS 영수증 UseCase (Port In)
 */
public interface ManagePmsReceiptUseCase {

    /** 룸서비스 주문 내역 생성 */
    void createReceipt(CreateReceiptCommand command);

    /** 특정 객실의 전체 영수증 조회 */
    List<GetPmsReceiptResult> getReceiptsByRoomNo(String roomNo);

    /** 특정 객실의 미결제 영수증 조회 */
    List<GetPmsReceiptResult> getUnpaidReceiptsByRoomNo(String roomNo);

    /** 개별 영수증 결제 */
    void payReceipt(Long receiptId);

    /** 객실 전체 일괄 결제 */
    void payAllByRoomNo(String roomNo);
}
