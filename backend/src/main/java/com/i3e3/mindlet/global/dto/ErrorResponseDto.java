package com.i3e3.mindlet.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "에러 응답 DTO", description = "API 호출이 실패하면 사용되는 DTO 입니다.")
public class ErrorResponseDto<T> {

    @Schema(name = "data", title = "데이터", description = "결과 데이터입니다.", defaultValue = "null", nullable = true)
    private final T data;

    @Builder
    public ErrorResponseDto(T data) {
        this.data = data;
    }
}
