package com.anook.backend.staff.request.application.port.in;

public interface ProcessStaffRequestUseCase {
    void accept(Long id, String staffName);
    void complete(Long id);
    void reject(Long id);
}
