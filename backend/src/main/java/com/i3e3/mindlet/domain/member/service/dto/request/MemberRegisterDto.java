package com.i3e3.mindlet.domain.member.service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"id", "password"})
public class MemberRegisterDto {

    private String id;

    private String password;

    @Builder
    public MemberRegisterDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
