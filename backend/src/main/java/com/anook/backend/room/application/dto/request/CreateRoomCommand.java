package com.anook.backend.room.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 객실 등록 요청 DTO
 */
public record CreateRoomCommand(
        @NotBlank(message = "객실 번호는 필수입니다.")
        String number,

        @NotNull(message = "객실 타입 ID는 필수입니다.")
        Long typeId
) {}
