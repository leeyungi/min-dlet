package com.i3e3.mindlet.domain.member.repository;

import com.i3e3.mindlet.domain.member.entity.AppConfig;
import com.i3e3.mindlet.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AppConfigRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AppConfigRepository appConfigRepository;

    private Member member1;

    private AppConfig appConfig1;

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
    }

    @Test
    @DisplayName("회원 식별키로 앱 설정 데이터 조회 - 앱 설정 데이터가 있는 경우")
    void findAppConfigSuccess() {
        //given
        Member savedMember = memberRepository.save(member1);
        AppConfig savedAppConfig = appConfigRepository.save(appConfig1);
        em.flush();
        em.clear();

        //when
        AppConfig findAppConfig = appConfigRepository.findByMemberSeq(savedMember.getSeq())
                .orElse(null);

        //then
        assertThat(findAppConfig.getSeq()).isEqualTo(savedAppConfig.getSeq());
        assertThat(findAppConfig.getMember().getSeq()).isEqualTo(savedMember.getSeq());
    }

    @Test
    @DisplayName("회원 식별키로 앱 설정 데이터 조회 - 회원 Seq 가 존재하지 않을 경우")
    void findAppConfigFailNonExistMember() {
        //given

        //when
        AppConfig appConfigNull = appConfigRepository.findByMemberSeq(0L)
                .orElse(null);
        //then
        assertThat(appConfigNull).isNull();
    }

    @Test
    @DisplayName("회원 식별키로 앱 설정 데이터 조회 - 회원 Seq 가 존재하고 deleted 가 true 인 경우")
    void findAppConfigFailExistMemberAndDeleted() {
        //given
        Member savedMember = memberRepository.save(member1);
        savedMember.delete();
        appConfigRepository.save(appConfig1);

        em.flush();
        em.clear();

        //when
        AppConfig appConfigNull = appConfigRepository.findByMemberSeq(savedMember.getSeq())
                .orElse(null);
        //then
        assertThat(appConfigNull).isNull();
    }
}