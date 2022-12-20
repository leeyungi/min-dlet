package com.i3e3.mindlet.domain.member.controller;

import com.i3e3.mindlet.domain.member.controller.dto.*;
import com.i3e3.mindlet.domain.member.entity.AppConfig;
import com.i3e3.mindlet.domain.member.service.MemberService;
import com.i3e3.mindlet.domain.member.service.dto.response.MemberInfoDto;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import com.i3e3.mindlet.global.dto.BaseResponseDto;
import com.i3e3.mindlet.global.dto.ErrorResponseDto;
import com.i3e3.mindlet.global.enums.Community;
import com.i3e3.mindlet.global.util.AuthenticationUtil;
import com.i3e3.mindlet.global.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "회원가입 API",
            description = "아이디, 패스워드를 받아 회원가입을 수행합니다.",
            tags = {"member"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponseDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "데이터 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BaseResponseDto<Void> register(@Validated @RequestBody RegisterRequestDto registerRequestDto) {
        String id = registerRequestDto.getId();
        String password = registerRequestDto.getPassword();
        if (password.contains(id)) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        } else if (memberService.isExistsId(id)) {
            throw new IllegalStateException(ErrorMessage.INVALID_ID.getMessage());
        }

        memberService.register(registerRequestDto.toServiceDto());

        return BaseResponseDto.<Void>builder()
                .build();
    }

    @Operation(
            summary = "아이디 중복 확인 API",
            description = "아이디 값을 받아 중복 확인 후 결과를 반환합니다.",
            tags = {"member"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "중복된 아이디",
                    content = @Content(schema = @Schema(implementation = BaseResponseDto.class))),
            @ApiResponse(
                    responseCode = "204",
                    description = "아이디 중복이 아님",
                    content = @Content(schema = @Schema(implementation = BaseResponseDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "데이터 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/id-duplicate-check/{id}")
    public ResponseEntity<BaseResponseDto<Void>> idDuplicateCheck(@PathVariable("id") String id) {
        String idRegx = "^[0-9|a-z|\\s]{4,12}$";
        if (!id.matches(idRegx)) {
            throw new IllegalStateException(ErrorMessage.INVALID_ID.getMessage() + " ID=" + id);
        }

        HttpStatus status = memberService.isExistsId(id) ?
                HttpStatus.OK : HttpStatus.NO_CONTENT;

        return new ResponseEntity<>(BaseResponseDto.<Void>builder()
                .build(), status);
    }

    @Operation(
            summary = "커뮤니티 변경 API",
            description = "인증 토큰, 회원 식별키, 커뮤니티 값을 받아 커뮤니티를 수정합니다.",
            tags = {"member"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "커뮤니티 변경 완료",
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
                    responseCode = "403",
                    description = "권한 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @PatchMapping("/{memberSeq}/community")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponseDto<Void> changeCommunity(@PathVariable Long memberSeq, @Validated @RequestBody CommunityModifyDto modifyDto) {
        AuthenticationUtil.verifyMember(memberSeq);

        Community community = null;
        try {
            community = Community.valueOf(modifyDto.getCommunity());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        memberService.changeCommunity(memberSeq, community);

        return BaseResponseDto.<Void>builder()
                .build();
    }

    @Operation(
            summary = "사운드 음소거 설정 API",
            description = "인증 토큰, 회원 식별키, 사운드 재생여부값을 받아 사운드를 On/Off 합니다.",
            tags = {"member"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "사운드 변경 완료",
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
                    responseCode = "403",
                    description = "권한 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @PatchMapping("/{memberSeq}/sound-off")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponseDto<Void> changeSound(@PathVariable Long memberSeq, @Validated @RequestBody SoundModifyDto modifyDto) {
        AuthenticationUtil.verifyMember(memberSeq);

        memberService.changeSound(memberSeq, modifyDto.isSoundOff());

        return BaseResponseDto.<Void>builder()
                .build();
    }

    @Operation(
            summary = "언어 변경 API",
            description = "인증 토큰, 회원 식별키, 선택 언어 값을 받아 언어를 변경 합니다.",
            tags = {"member"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "언어 변경 완료",
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
                    responseCode = "403",
                    description = "권한 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @PatchMapping("/{memberSeq}/language")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponseDto<Void> changeLanguage(@PathVariable Long memberSeq, @Validated @RequestBody LanguageModifyDto modifyDto) {
        AuthenticationUtil.verifyMember(memberSeq);

        AppConfig.Language language = null;

        try {
            language = AppConfig.Language.valueOf(modifyDto.getLanguage());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        memberService.changeLanguage(memberSeq, language);

        return BaseResponseDto.<Void>builder()
                .build();
    }

    @Operation(
            summary = "로그인 API",
            description = "아이디, 패스워드를을 받아 로그인합니다.",
            tags = {"member"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponseDto.class))),
            @ApiResponse(
                    responseCode = "204",
                    description = "아이디 또는 패스워드가 틀림",
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
    @PostMapping("/login")
    public ResponseEntity<BaseResponseDto<MemberInfoDto>> login(@Validated @RequestBody LoginRequestDto loginRequestDto) {
        HttpStatus status = null;
        MemberInfoDto memberInfo = null;
        if (memberService.login(loginRequestDto.toServiceDto())) {
            memberInfo = memberService.getMemberInfoById(loginRequestDto.getId());

            String jwtToken = JwtTokenUtil.getToken(memberInfo.getSeq(), memberInfo.getId());
            memberInfo.provideToken(jwtToken);

            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<>(BaseResponseDto.<MemberInfoDto>builder()
                .data(memberInfo)
                .build(), status);
    }

    @Operation(
            summary = "회원 정보 조회 API",
            description = "인증 토큰, 회원 식별키를 받고 회원 정보를 반환합니다.",
            tags = {"member"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
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
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{memberSeq}")
    public BaseResponseDto<MemberInfoDto> memberInfo(@PathVariable Long memberSeq) {
        AuthenticationUtil.verifyMember(memberSeq);

        MemberInfoDto memberInfo = memberService.getMemberInfoBySeq(memberSeq);

        return BaseResponseDto.<MemberInfoDto>builder()
                .data(memberInfo)
                .build();
    }

    @Operation(
            summary = "회원 탈퇴 API",
            description = "인증 토큰, 회원 식별키를 받고 회원 탈퇴 처리 합니다.",
            tags = {"member"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "회원 탈퇴 성공",
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{memberSeq}")
    public BaseResponseDto<Void> delete(@PathVariable("memberSeq") Long memberSeq) {
        AuthenticationUtil.verifyMember(memberSeq);

        memberService.delete(memberSeq);

        return BaseResponseDto.<Void>builder()
                .build();
    }
}
