package com.i3e3.mindlet.domain.admin.controller.form;

import com.i3e3.mindlet.domain.admin.service.dto.AdminRegisterDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString(of = {"id", "password", "key"})
public class RegisterForm {

    @Pattern(message = "아이디를 영문 소문자, 숫자를 포함하여 4자 이상 12자 이하로 입력해주세요.", regexp = "^^(?=.*[a-z])(?=.*\\d)[a-z\\d]{4,12}$")
    private String id;

    @Pattern(message = "패스워드를 영문 대소문자, 숫자, 특수문자를 포함하여 8자 이상 20자 이하로 입력해주세요.", regexp = "^((?=.*[a-z])(?=.*\\d)((?=.*\\W)|(?=.*[A-Z]))|(?=.*\\W)(?=.*[A-Z])((?=.*\\d)|(?=.*[a-z]))).{8,20}$")
    private String password;

    @NotEmpty(message = "회원가입 키는 필수입니다.")
    private String key;

    public RegisterForm() {
    }

    @Builder
    public RegisterForm(String id, String password, String key) {
        this.id = id;
        this.password = password;
        this.key = key;
    }

    public AdminRegisterDto toDto() {
        return AdminRegisterDto.builder()
                .id(id)
                .password(password)
                .key(key)
                .build();
    }
}
