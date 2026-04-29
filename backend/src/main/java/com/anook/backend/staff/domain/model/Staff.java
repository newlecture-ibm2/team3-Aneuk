package com.anook.backend.staff.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Staff {
    private Long id;
    private String name;
    private String pin;
    private Long roleId;
    private Department department;
}
