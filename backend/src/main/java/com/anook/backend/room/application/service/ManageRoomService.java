package com.anook.backend.room.application.service;

import com.anook.backend.room.application.dto.request.CreateRoomCommand;
import com.anook.backend.room.application.dto.response.GetRoomResult;
import com.anook.backend.room.application.port.in.ManageRoomUseCase;
import com.anook.backend.room.application.port.out.RoomRepositoryPort;
import com.anook.backend.room.domain.model.Room;
import com.anook.backend.global.exception.BusinessException;
import com.anook.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 객실 관리 서비스 (CRUD)
 *
 * ❌ JPA Repository 직접 import 금지
 * ✅ Port 인터페이스만 의존
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ManageRoomService implements ManageRoomUseCase {

    private final RoomRepositoryPort roomRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GetRoomResult> getAll() {
        return roomRepository.findAll().stream()
                .map(GetRoomResult::from)
                .toList();
    }

    @Override
    public GetRoomResult create(CreateRoomCommand command) {
        // 객실 번호 중복 확인
        if (roomRepository.existsByNumber(command.number())) {
            throw new BusinessException(ErrorCode.DUPLICATE_ROOM_NUMBER);
        }

        Room room = Room.create(command.number(), command.typeId());
        Room saved = roomRepository.save(room);
        return GetRoomResult.from(saved);
    }

    @Override
    public void delete(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new BusinessException(ErrorCode.ROOM_NOT_FOUND, "roomId=" + roomId);
        }
        roomRepository.deleteById(roomId);
    }
}
