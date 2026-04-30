package com.anook.backend.pms.application.dto.request;

/**
 * 룸서비스 주문 내역 생성 Command
 */
public record CreateReceiptCommand(
        String roomNo,
        Long menuId,
        int quantity
) {}
