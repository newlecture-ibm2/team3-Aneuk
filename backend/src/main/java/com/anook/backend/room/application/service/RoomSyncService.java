package com.anook.backend.room.application.service;

import com.anook.backend.room.application.port.out.PmsRoomQueryPort;
import com.anook.backend.room.application.port.out.RoomRepositoryPort;
import com.anook.backend.room.domain.model.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 객실 목록 자동 동기화 서비스
 *
 * 앱 시작 시 pms_room 테이블의 객실 번호를 읽어
 * ANOOK room 테이블에 없는 객실을 자동으로 추가한다.
 *
 * 이를 통해 data.sql에서 ANOOK room INSERT를 빼더라도
 * PMS 데이터를 기반으로 자동 동기화된다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RoomSyncService {

    private final PmsRoomQueryPort pmsRoomQueryPort;
    private final RoomRepositoryPort roomRepositoryPort;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void syncRoomsFromPms() {
        List<String> pmsNumbers = pmsRoomQueryPort.findAllRoomNumbers();
        int synced = 0;

        for (String number : pmsNumbers) {
            if (!roomRepositoryPort.existsByNumber(number)) {
                roomRepositoryPort.save(new Room(number));
                synced++;
            }
        }

        log.info("[RoomSync] PMS 객실 {}개 확인 → ANOOK에 {}개 신규 동기화 완료",
                pmsNumbers.size(), synced);
    }
}
