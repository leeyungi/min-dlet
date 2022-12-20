package com.i3e3.mindlet.domain.dandelion.controller;

import com.i3e3.mindlet.domain.admin.entity.Report;
import com.i3e3.mindlet.domain.dandelion.controller.dto.ReportReasonDto;
import com.i3e3.mindlet.domain.dandelion.service.PetalService;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import com.i3e3.mindlet.global.dto.BaseResponseDto;
import com.i3e3.mindlet.global.dto.ErrorResponseDto;
import com.i3e3.mindlet.global.util.AuthenticationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/petals")
public class PetalController {

    private final PetalService petalService;

    @Operation(
            summary = "신고 접수 API",
            description = "인증 토큰, 꽃잎 식별키, 신고 사유를 전달받고 신고테이블에 저장합니다.",
            tags = {"dandelion"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "신고 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponseDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "데이터 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "토큰 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @PostMapping("/{petalSeq}/reports")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponseDto<Void> report(@PathVariable Long petalSeq,
                                        @Validated @RequestBody ReportReasonDto reportDto) {
        Report.Reason reason = null;

        try {
            reason = Report.Reason.valueOf(reportDto.getReason());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        Long findMemberSeq = AuthenticationUtil.getMemberSeq();

        petalService.reportPetal(findMemberSeq, petalSeq, reason);

        return BaseResponseDto.<Void>builder()
                .build();
    }

    @Operation(
            summary = "꽃잎 삭제 API",
            description = "인증 토큰, 꽃잎 식별키를 전달받고 꽃잎을 삭제합니다.",
            tags = {"dandelion"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "신고 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponseDto.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "토큰 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @DeleteMapping("/{petalSeq}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseResponseDto<Void> deletePetal(@PathVariable Long petalSeq) {

        Long memberSeq = AuthenticationUtil.getMemberSeq();

        if (!petalService.isDandelionOwnerByPetal(memberSeq, petalSeq)) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        petalService.deletePetal(petalSeq);

        return BaseResponseDto.<Void>builder()
                .build();
    }
}
