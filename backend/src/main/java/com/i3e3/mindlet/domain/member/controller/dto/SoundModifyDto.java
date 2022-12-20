package com.i3e3.mindlet.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(name = "사운드 변경 DTO", description = "사운드 변경 API를 호출할 때 사용됩니다.")
@Getter
@ToString(of = {"soundOff"})
public class SoundModifyDto {

    @Schema(title = "사운드", description = "사운드 재생 여부 데이터를 담는 필드입니다.", example = "true", required = true)
    @NotNull
    private boolean soundOff;
}