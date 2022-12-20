package com.i3e3.mindlet.domain.dandelion.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Schema(name = "꽃잎 신고 사유 DTO", description = "꽃잎 컨텐츠 신고 API를 호출할 때 사용됩니다.")
@Getter
@ToString(of = {"reason"})
public class ReportReasonDto {

    @Schema(title = "신고 사유", description = "신고 사유를 담는 필드입니다.", example = "ALBUM")
    @NotBlank
    private String reason;
}
