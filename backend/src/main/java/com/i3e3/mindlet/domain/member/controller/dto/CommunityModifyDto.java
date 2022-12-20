package com.i3e3.mindlet.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Schema(name = "커뮤니티 변경 DTO", description = "커뮤니티 변경 API를 호출할 때 사용됩니다.")
@Getter
@ToString(of = {"community"})
public class CommunityModifyDto {

    @Schema(title = "커뮤니티", description = "커뮤니티 데이터를 담는 필드입니다.", example = "WORLD", required = true)
    @NotBlank
    private String community;
}

