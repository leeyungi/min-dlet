package com.i3e3.mindlet.domain.dandelion.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Schema(name = "민들레 태그 네임 DTO", description = "민들레 태그 추가 API를 호출할 때 사용됩니다.")
@Getter
@ToString(of = {"name"})
public class DandelionTagRegisterDto {

    @Schema(title = "태그 네임", description = "태그 네임 담는 필드입니다.", example = "태그1", required = true)
    @Length(min = 1, max = 12)
    @NotBlank
    private String name;
}
