package com.anook.backend.room.application.service;

import com.anook.backend.room.application.dto.response.GetRoomResult;
import com.anook.backend.room.application.port.in.GetRoomUseCase;
import com.anook.backend.room.application.port.out.RoomRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 객실 조회 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetRoomService implements GetRoomUseCase {

    private final RoomRepositoryPort roomRepository;

    @Override
    public List<GetRoomResult> getAll() {
        return roomRepository.findAll().stream()
                .map(GetRoomResult::from)
                .toList();
    }
}
