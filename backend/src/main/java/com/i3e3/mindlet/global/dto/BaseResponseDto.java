package com.i3e3.mindlet.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "기본 응답 DTO", description = "API 호출이 성공하면 사용되는 DTO 입니다.")
public class BaseResponseDto<T> {

    @Schema(name = "data", title = "데이터", description = "결과 데이터입니다.", defaultValue = "null", nullable = true)
    private final T data;

    @Builder
    public BaseResponseDto(T data) {
        this.data = data;
    }
}
