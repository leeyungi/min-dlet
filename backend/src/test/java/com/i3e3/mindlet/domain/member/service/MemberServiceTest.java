package com.i3e3.mindlet.domain.member.service;

import com.i3e3.mindlet.domain.dandelion.controller.dto.DandelionRegisterDto;
import com.i3e3.mindlet.domain.dandelion.controller.dto.PetalRegisterDto;
import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Petal;
import com.i3e3.mindlet.domain.dandelion.repository.TagRepository;
import com.i3e3.mindlet.domain.dandelion.service.DandelionService;
import com.i3e3.mindlet.domain.dandelion.service.TagService;
import com.i3e3.mindlet.domain.member.controller.dto.LoginRequestDto;
import com.i3e3.mindlet.domain.member.controller.dto.RegisterRequestDto;
import com.i3e3.mindlet.domain.member.entity.AppConfig;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.repository.AppConfigRepository;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.domain.member.service.dto.response.MemberInfoDto;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import com.i3e3.mindlet.global.enums.Community;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DandelionService dandelionService;

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;


    private Member member1;

    private AppConfig appConfig1;

    private RegisterRequestDto registerRequestDto1;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        appConfigRepository.deleteAll();

        em.flush();
        em.clear();

        member1 = Member.builder()
                .id("아이디1")
                .password("패스워드1")
                .build();

        appConfig1 = AppConfig.builder()
                .member(member1)
                .language(AppConfig.Language.ENGLISH)
                .build();

        registerRequestDto1 = RegisterRequestDto.builder()
                .id("id01")
                .password("pass12#$")
                .build();
    }

    @Test
    @DisplayName("회원 식별키로 커뮤니티 변경 - 성공")
    void changeCommunitySuccess() {

        //given
        Member savedMember = memberRepository.save(member1);
        appConfig1.changeCommunity(Community.WORLD);
        appConfigRepository.save(appConfig1);

        em.flush();
        em.clear();

        //when
        memberService.changeCommunity(savedMember.getSeq(), Community.KOREA);
        AppConfig changedAppConfig = appConfigRepository.findByMemberSeq(savedMember.getSeq())
                .orElse(null);

        //then
        assertThat(changedAppConfig.getCommunity()).isEqualTo(Community.KOREA);
        assertThat(changedAppConfig.getMember().getSeq()).isEqualTo(savedMember.getSeq());
    }

    @Test
    @DisplayName("회원 식별키로 커뮤니티 변경 - 회원 정보가 없을 때")
    void changeCommunityFailNonMember() {
        //given

        //when

        //then
        assertThatThrownBy(() -> memberService.changeCommunity(0L, Community.KOREA))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("회원 식별키로 커뮤니티 변경 - 회원 정보가 있는데 deleted 가 true")
    void changeCommunityFailExistMemberAndDeleted() {
        //given
        Member savedMember = memberRepository.save(member1);
        appConfigRepository.save(appConfig1);
        savedMember.delete();
        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> memberService.changeCommunity(savedMember.getSeq(), Community.KOREA))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("회원 식별키로 사운드 변경 성공 - 음소거 on")
    void changeSoundSuccessMuteOn() {
        //given
        Member savedMember = memberRepository.save(member1);
        appConfigRepository.save(appConfig1);

        em.flush();
        em.clear();

        //when
        memberService.changeSound(savedMember.getSeq(), true);
        AppConfig changedAppConfig = appConfigRepository.findByMemberSeq(savedMember.getSeq())
                .orElse(null);

        //then
        assertThat(changedAppConfig.isSoundOff()).isTrue();
    }

    @Test
    @DisplayName("회원 식별키로 사운드 변경 성공 - 음소거 Off")
    void changeSoundSuccessMuteOff() {
        //given
        Member savedMember = memberRepository.save(member1);
        AppConfig savedConfig = appConfigRepository.save(appConfig1);
        savedConfig.soundOff();

        em.flush();
        em.clear();

        //when
        memberService.changeSound(savedMember.getSeq(), false);
        AppConfig changedAppConfig = appConfigRepository.findByMemberSeq(savedMember.getSeq())
                .orElse(null);

        //then
        assertThat(changedAppConfig.isSoundOff()).isFalse();
    }

    @Test
    @DisplayName("회원 식별키로 사운드 변경 실패 - 회원 정보가 없을 때")
    void changeSoundFailNonMember() {
        //given

        //when

        //then
        assertThatThrownBy(() -> memberService.changeSound(0L, true))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
        assertThatThrownBy(() -> memberService.changeSound(0L, false))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("회원 식별키로 사운드 변경 실패 - 회원 정보가 있는데 deleted 가 true")
    void changeSoundFailExistMemberAndDeleted() {
        //given
        Member savedMember = memberRepository.save(member1);
        appConfigRepository.save(appConfig1);
        savedMember.delete();
        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> memberService.changeSound(savedMember.getSeq(), true))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
        assertThatThrownBy(() -> memberService.changeSound(savedMember.getSeq(), false))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("아이디 중복 확인 - 중복된 경우 : 회원 데이터 있음")
    void checkIdDuplicateTrue() {
        // given
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        boolean isExist = memberService.isExistsId(savedMember.getId());

        // then
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("아이디 중복 확인 - 중복된 경우 : 회원 데이터 있음 및 회원 탈퇴")
    void checkIdDuplicateTrueWhenDeletedMember() {
        // given
        member1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        boolean isExist = memberService.isExistsId(savedMember.getId());

        // then
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("아이디 중복 확인 - 중복이 아닌 경우 : 회원 데이터 없음")
    void checkIdDuplicateFalseNotExistMember() {
        // given

        // when
        boolean isExist = memberService.isExistsId("no1234");

        // then
        assertThat(isExist).isFalse();
    }

    @Test
    @DisplayName("회원가입 - 성공")
    void registerSuccess() {
        // given

        // when
        Member savedMember = memberService.register(registerRequestDto1.toServiceDto());

        // then
        Member findMember = memberRepository.findBySeq(savedMember.getSeq())
                .orElse(null);
        assertThat(findMember.getId()).isEqualTo(registerRequestDto1.getId());
        assertThat(passwordEncoder.matches(registerRequestDto1.getPassword(), findMember.getPassword())).isTrue();

        AppConfig findAppConfig = findMember.getAppConfig();
        assertThat(findAppConfig.getLanguage()).isEqualTo(AppConfig.Language.KOREAN);
        assertThat(findAppConfig.getCommunity()).isEqualTo(Community.KOREA);
        assertThat(findAppConfig.getMember().getSeq()).isEqualTo(savedMember.getSeq());
    }

    @Test
    @DisplayName("로그인 - 성공")
    void loginSuccess() {
        // given
        memberService.register(registerRequestDto1.toServiceDto());
        LoginRequestDto newLoginRequestDto = LoginRequestDto.builder()
                .id(registerRequestDto1.getId())
                .password(registerRequestDto1.getPassword())
                .build();

        // when
        boolean isLogin = memberService.login(newLoginRequestDto.toServiceDto());

        // then
        assertThat(isLogin).isTrue();
    }

    @Test
    @DisplayName("로그인 - 실패 : 회원 데이터가 없는 경우")
    void loginFailWhenNotExistMember() {
        // given
        LoginRequestDto newLoginRequestDto = LoginRequestDto.builder()
                .id(registerRequestDto1.getId())
                .password(registerRequestDto1.getPassword())
                .build();

        // when
        boolean isLogin = memberService.login(newLoginRequestDto.toServiceDto());

        // then
        assertThat(isLogin).isFalse();
    }

    @Test
    @DisplayName("로그인 - 실패 : 패스워드를 잘못 입력한 경우")
    void loginFailWhenInvalidPassword() {
        // given
        memberService.register(registerRequestDto1.toServiceDto());
        LoginRequestDto newLoginRequestDto = LoginRequestDto.builder()
                .id(registerRequestDto1.getId())
                .password(registerRequestDto1.getPassword() + "invalid")
                .build();

        // when
        boolean isLogin = memberService.login(newLoginRequestDto.toServiceDto());

        // then
        assertThat(isLogin).isFalse();
    }

    @Test
    @DisplayName("회원 아이디로 회원 정보 조회 - 데이터가 있는 경우")
    void findMemberInfoById() {
        // given
        Member savedMember = memberService.register(registerRequestDto1.toServiceDto());

        // when
        MemberInfoDto findMemberInfo = memberService.getMemberInfoById(savedMember.getId());

        // then
        assertThat(findMemberInfo.getSeq()).isEqualTo(savedMember.getSeq());
        assertThat(findMemberInfo.getCommunity()).isEqualTo(savedMember.getAppConfig().getCommunity());
        assertThat(findMemberInfo.getLanguage()).isEqualTo(savedMember.getAppConfig().getLanguage());
        assertThat(findMemberInfo.getRole()).isEqualTo(savedMember.getRole());
    }

    @Test
    @DisplayName("회원 아이디로 회원 정보 조회 - 데이터가 있지만 삭제 처리된 경우")
    void findMemberInfoByIdWHenDeleted() {
        // given
        Member savedMember = memberService.register(registerRequestDto1.toServiceDto());
        savedMember.delete();
        em.flush();
        em.clear();

        // when

        // then
        assertThatThrownBy(() -> memberService.getMemberInfoById(savedMember.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_ID.getMessage());
    }

    @Test
    @DisplayName("회원 아이디로 회원 정보 조회 - 회원 데이터가 없는 경우")
    void findMemberInfoByIdWhenNotExistMember() {
        // given


        // when

        // then
        assertThatThrownBy(() -> memberService.getMemberInfoById("id01"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_ID.getMessage());
    }

    @Test
    @DisplayName("언어 변경 - 성공")
    void changeLanguageSuccess() {

        //given
        Member savedMember = memberRepository.save(member1);
        AppConfig savedAppConfig = appConfigRepository.save(appConfig1);
        savedAppConfig.changeLanguage(AppConfig.Language.ENGLISH);
        em.flush();
        em.clear();

        //when
        memberService.changeLanguage(savedMember.getSeq(), AppConfig.Language.KOREAN);
        AppConfig findAppConfig = appConfigRepository.findByMemberSeq(savedMember.getSeq())
                .orElse(null);

        //then
        assertThat(findAppConfig.getLanguage()).isEqualTo(AppConfig.Language.KOREAN);
    }

    @Test
    @DisplayName("언어 변경 - 실패 / 회원이 null")
    void changeLanguageFailNotExistMember() {
        //given

        //when

        //then
        assertThatThrownBy(() -> memberService.changeLanguage(0L, AppConfig.Language.KOREAN))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("언어 변경 - 실패 / 회원이 deleted")
    void changeLanguageFailExistMemberDeleted() {
        //given
        Member savedMember = memberRepository.save(member1);
        appConfigRepository.save(appConfig1);
        savedMember.delete();

        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> memberService.changeLanguage(savedMember.getSeq(), AppConfig.Language.KOREAN))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("회원 식별키로 회원 정보 조회 - 데이터가 있는 경우")
    void findMemberInfoBySeq() {
        // given
        Member savedMember = memberService.register(registerRequestDto1.toServiceDto());

        // when
        MemberInfoDto findMemberInfo = memberService.getMemberInfoBySeq(savedMember.getSeq());

        // then
        assertThat(findMemberInfo.getSeq()).isEqualTo(savedMember.getSeq());
        assertThat(findMemberInfo.getCommunity()).isEqualTo(savedMember.getAppConfig().getCommunity());
        assertThat(findMemberInfo.getLanguage()).isEqualTo(savedMember.getAppConfig().getLanguage());
        assertThat(findMemberInfo.getRole()).isEqualTo(savedMember.getRole());
    }

    @Test
    @DisplayName("회원 식별키로 회원 정보 조회 - 데이터가 있지만 삭제 처리된 경우")
    void findMemberInfoBySeqWHenDeleted() {
        // given
        Member savedMember = memberService.register(registerRequestDto1.toServiceDto());
        savedMember.delete();
        em.flush();
        em.clear();

        // when

        // then
        assertThatThrownBy(() -> memberService.getMemberInfoBySeq(savedMember.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("회원 아이디로 회원 정보 조회 - 회원 데이터가 없는 경우")
    void findMemberInfoFailWhenNotExistMember() {
        // given

        // when

        // then
        assertThatThrownBy(() -> memberService.getMemberInfoBySeq(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("회원 탈퇴 - 예외 발생 : 회원 데이터 없음")
    void deleteMemberExceptionWhenNotExistsMember() {
        // given

        // when

        // then
        assertThatThrownBy(() -> memberService.delete(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("회원 탈퇴 - 예외 발생 : 이미 삭제된 회원")
    void deleteMemberExceptionWhenDeletedMember() {
        // given
        member1.delete();
        Member savedMember1 = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when

        // then
        assertThatThrownBy(() -> memberService.delete(savedMember1.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("회원 탈퇴 - 성공 : 데이터 검증")
    void deleteMemberSuccess() throws IOException {
        // given
        /**
         * 회원1 회원가입
         * 회원1을 탈퇴 처리합니다.
         */
        Member savedMember1 = memberService.register(RegisterRequestDto.builder()
                .id("id01")
                .password("pass12#$")
                .build().toServiceDto());

        /**
         * 회원2 회원가입
         */
        Member savedMember2 = memberService.register(RegisterRequestDto.builder()
                .id("id02")
                .password("pass12#$")
                .build().toServiceDto());

        /**
         * 회원1 계정으로 민들레, 꽃잎 생성
         * 회원1 계정이 삭제 처리되면 이 데이터들은 삭제 처리되어야 한다.
         */
        Dandelion savedDandelionByMember1 = dandelionService.createDandelion(savedMember1.getSeq(), DandelionRegisterDto.builder()
                .blossomedDate("2022-12-30")
                .message("messageByMember1")
                .imageFile(null)
                .build().toSvcDto());

        /**
         * 회원2 계정으로 민들레, 꽃잎 생성
         * 회원1 계정이 삭제 처리되어도 이 데이터는 존재해야 한다.
         */
        Dandelion savedDandelionByMember2 = dandelionService.createDandelion(savedMember2.getSeq(), DandelionRegisterDto.builder()
                .blossomedDate("2022-12-30")
                .message("messageByMember2")
                .imageFile(null)
                .build().toSvcDto());

        /**
         * 회원2 계정으로 회원1 계정의 민들레에 꽃잎을 추가한다.
         * 회원1 계정이 삭제 처리되면 이 데이터는 삭제 처리되어야 한다.
         */
        dandelionService.addPetal(savedMember2.getSeq(), savedDandelionByMember1.getSeq(), PetalRegisterDto.builder()
                .imageFile(null)
                .message("messageByMember2")
                .build().toSvcDto());

        /**
         * 회원1 계정으로 회원2 계정의 민들레에 꽃잎을 추가한다.
         * 회원1 계정이 삭제 처리되면 이 데이터는 삭제 처리되어야 한다.
         */
        dandelionService.addPetal(savedMember1.getSeq(), savedDandelionByMember2.getSeq(), PetalRegisterDto.builder()
                .imageFile(null)
                .message("messageByMember1")
                .build().toSvcDto());

        /**
         * 회원2 계정으로 회원1 계정의 민들레에 태그를 추가한다.
         * 회원1 계정이 삭제 처리되면 이 데이터는 완전 삭제되어야 한다.
         */
        tagService.registerDandelionTag(savedDandelionByMember1.getSeq(), savedMember2.getSeq(), "tg1ByMember2");
        tagService.registerDandelionTag(savedDandelionByMember1.getSeq(), savedMember2.getSeq(), "tg2ByMember2");

        /**
         * 회원1 계정으로 회원2 계정의 민들레에 태그를 추가한다.
         * 회원1 계정이 삭제 처리되면 이 데이터는 완전 삭제되어야 한다.
         */
        tagService.registerDandelionTag(savedDandelionByMember2.getSeq(), savedMember1.getSeq(), "tg1ByMember1");
        tagService.registerDandelionTag(savedDandelionByMember2.getSeq(), savedMember1.getSeq(), "tg2ByMember1");

        // when
        memberService.delete(savedMember1.getSeq());

        // then
        /**
         * 회원 식별키로 데이터를 조회(findBySeq: 삭데 처리된 데이터는 조회하지 않는다.)하면 예외가 발생해야 한다.
         */
        assertThatThrownBy(() -> memberRepository.findBySeq(savedMember1.getSeq())
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage())))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());

        Member deletedMember = memberRepository.findBySeqContainsDeleted(savedMember1.getSeq())
                .orElse(null);

        /**
         * 삭제 처리 검증
         */
        assertThat(deletedMember.isDeleted()).isTrue();

        /**
         * 1. 회원1이 생성한 민들레 삭제 처리 검증
         * 2. 회원1이 생성한 민들레에 추가되었던 꽃잎 삭제 처리 검증
         * 3. 회원1이 생성한 민들레게 추가되었던 태그 리스트 사이즈 0 검증
         */
        for (Dandelion dandelion : deletedMember.getDandelions()) {
            assertThat(dandelion.isDeleted()).isTrue();

            for (Petal petal : dandelion.getPetals()) {
                assertThat(petal.isDeleted()).isTrue();
            }

            assertThat(dandelion.getTags().size()).isEqualTo(0);
        }

        /**
         * 회원 1이 참여하였던 민들레의 꽃잎 삭제 처리 검증
         */
        for (Petal petal : deletedMember.getPetals()) {
            assertThat(petal.isDeleted()).isTrue();
        }

        /**
         * 회원1이 참여하였던 민들레들의 태그 리스트 사이즈 0 검증
         */
        assertThat(deletedMember.getTags().size()).isEqualTo(0);
    }
}