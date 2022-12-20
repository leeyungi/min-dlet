package com.i3e3.mindlet.domain.admin.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"id", "password", "key"})
public class AdminRegisterDto {

    private final String id;

    private final String password;

    private final String key;

    @Builder
    public AdminRegisterDto(String id, String password, String key) {
        this.id = id;
        this.password = password;
        this.key = key;
    }
}
