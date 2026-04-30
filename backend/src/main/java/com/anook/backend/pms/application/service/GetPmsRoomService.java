package com.anook.backend.pms.application.service;

import com.anook.backend.pms.application.dto.response.GetPmsRoomResult;
import com.anook.backend.pms.application.port.in.GetPmsRoomUseCase;
import com.anook.backend.pms.application.port.out.PmsRoomRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * PMS 객실 조회 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPmsRoomService implements GetPmsRoomUseCase {

    private final PmsRoomRepositoryPort pmsRoomRepository;

    @Override
    public List<GetPmsRoomResult> getAllRooms() {
        return pmsRoomRepository.findAllWithOccupancy().stream()
                .map(room -> new GetPmsRoomResult(
                        room.getNumber(),
                        room.getType(),
                        room.isOccupied(),
                        room.getGuestName()
                ))
                .toList();
    }
}
