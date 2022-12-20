package com.i3e3.mindlet.domain.member.controller.dto;

import com.i3e3.mindlet.domain.member.service.dto.request.MemberLoginDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Pattern;

@Schema(name = "로그인 요청 DTO", description = "로그인 API를 호출할 때 사용됩니다.")
@NoArgsConstructor
@Getter
@ToString(of = {"id", "password"})
public class LoginRequestDto {

    @Schema(title = "아이디", description = "아이디입니다.", example = "good", minLength = 4, maxLength = 12, required = true)
    @Pattern(message = "", regexp = "^[0-9|a-z|\\s]{4,12}$")
    private String id;

    @Schema(title = "패스워드", description = "패스워드입니다.", example = "test12#$", minLength = 8, maxLength = 20, required = true)
    @Pattern(message = "", regexp = "^((?=.*[a-z])(?=.*\\d)((?=.*\\W))|(?=.*\\W)((?=.*\\d)|(?=.*[a-z]))).{8,20}$")
    private String password;

    @Builder
    public LoginRequestDto(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public MemberLoginDto toServiceDto() {
        return MemberLoginDto.builder()
                .id(id)
                .password(password)
                .build();
    }
}
