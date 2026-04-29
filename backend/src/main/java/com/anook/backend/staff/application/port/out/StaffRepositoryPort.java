package com.anook.backend.staff.application.port.out;

import com.anook.backend.staff.domain.model.Staff;

import java.util.Optional;

public interface StaffRepositoryPort {
    Optional<Staff> findByPin(String pin);
}
