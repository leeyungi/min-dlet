package com.i3e3.mindlet.domain.member.service.dto.response;

import com.i3e3.mindlet.domain.member.entity.AppConfig;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.global.enums.Community;
import com.i3e3.mindlet.global.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Schema(name = "회원 정보 DTO", description = "회원 정보를 반환하기 위해 사용됩니다.")
@Getter
@ToString
public class MemberInfoDto {

    private Long seq;

    private String id;

    private AppConfig.Language language;

    private Community community;

    private boolean soundOff;

    private Role role;

    private String jwtToken;

    @Builder
    public MemberInfoDto(Long seq, String id, AppConfig.Language language, Community community, boolean soundOff, Role role) {
        this.seq = seq;
        this.id = id;
        this.language = language;
        this.community = community;
        this.soundOff = soundOff;
        this.role = role;
    }

    static public MemberInfoDto createByMember(Member member) {
        AppConfig appConfig = member.getAppConfig();
        return MemberInfoDto.builder()
                .seq(member.getSeq())
                .id(member.getId())
                .language(appConfig.getLanguage())
                .community(appConfig.getCommunity())
                .soundOff(appConfig.isSoundOff())
                .role(member.getRole())
                .build();
    }

    public void provideToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
