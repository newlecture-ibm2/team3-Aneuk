package com.anook.backend.request.application.port.in;

public interface UpdateStaffRequestUseCase {
    void accept(Long id, String staffName);
    void complete(Long id);
    void reject(Long id);
}
