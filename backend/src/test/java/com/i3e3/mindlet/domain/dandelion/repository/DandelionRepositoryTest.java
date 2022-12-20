package com.i3e3.mindlet.domain.dandelion.repository;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Petal;
import com.i3e3.mindlet.domain.member.entity.AppConfig;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.entity.MemberDandelionHistory;
import com.i3e3.mindlet.domain.member.repository.AppConfigRepository;
import com.i3e3.mindlet.domain.member.repository.MemberDandelionHistoryRepository;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.enums.Community;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DandelionRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DandelionRepository dandelionRepository;

    @Autowired
    private PetalRepository petalRepository;

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Autowired
    private MemberDandelionHistoryRepository memberDandelionHistoryRepository;

    private Member member1, member2, member3;

    private AppConfig appConfig1, appConfig2, appConfig3;

    private Dandelion dandelion1, dandelion2, dandelion3;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        dandelionRepository.deleteAll();
        em.flush();
        em.clear();

        member1 = Member.builder()
                .id("아이디1")
                .password("패스워드1")
                .build();

        member2 = Member.builder()
                .id("아이디2")
                .password("패스워드2")
                .build();

        member3 = Member.builder()
                .id("아이디3")
                .password("패스워드3")
                .build();

        appConfig1 = AppConfig.builder()
                .member(member1)
                .language(AppConfig.Language.ENGLISH)
                .build();

        appConfig2 = AppConfig.builder()
                .member(member2)
                .language(AppConfig.Language.ENGLISH)
                .build();

        appConfig3 = AppConfig.builder()
                .member(member3)
                .language(AppConfig.Language.ENGLISH)
                .build();

        dandelion1 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(member1.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(member1)
                .build();

        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-05-01"))
                .community(member2.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(member2)
                .build();

        dandelion3 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-05-02"))
                .community(member3.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(member3)
                .build();
    }

    @Test
    @DisplayName("민들레 엔티티 조회 - 데이터가 존재하는 경우")
    void findDandelionSuccessExist() {
        // given
        memberRepository.save(member1);
        Dandelion savedDandelion = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        Dandelion findDandelion = dandelionRepository.findBySeq(savedDandelion.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion).isNotNull();
        assertThat(findDandelion.getBlossomedDate()).isEqualTo(dandelion1.getBlossomedDate());
        assertThat(findDandelion.getCommunity()).isEqualTo(dandelion1.getCommunity());
        assertThat(findDandelion.getFlowerSignNumber()).isEqualTo(dandelion1.getFlowerSignNumber());
        assertThat(findDandelion.getMember().getSeq()).isEqualTo(dandelion1.getMember().getSeq());
    }

    @Test
    @DisplayName("민들레 엔티티 조회 - 데이터가 없는 경우")
    void findDandelionSuccessNonExist() {
        // given

        // when
        Dandelion findDandelion = dandelionRepository.findBySeq(0L)
                .orElse(null);

        // then
        assertThat(findDandelion).isNull();
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - 사용중인 민들레가 0개인 경우")
    void countUsingSeedWhenNothing() {
        // given
        member1.getDandelions().clear();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(0);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - flying 상태의 민들레가 1개인 경우")
    void countUsingSeedWhenFlying() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.FLYING);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(1);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - flying 상태의 민들레가 1개이지만 삭제된 경우")
    void countUsingSeedWhenFlyingAndDeleted() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.FLYING);
        dandelion1.delete();
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(0);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - hold 상태의 민들레가 1개인 경우")
    void countUsingSeedWhenHold() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.HOLD);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(1);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - hold 상태의 민들레가 1개이지만 삭제된 경우")
    void countUsingSeedWhenHoldAndDeleted() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.HOLD);
        dandelion1.delete();
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(0);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - return 상태의 민들레가 1개인 경우")
    void countUsingSeedWhenReturn() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.RETURN);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(1);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - return 상태의 민들레가 1개이지만 삭제된 경우")
    void countUsingSeedWhenReturnAndDeleted() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.RETURN);
        dandelion1.delete();
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(0);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - pending 상태의 민들레가 1개인 경우")
    void countUsingSeedWhenPending() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.PENDING);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(1);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - pending 상태의 민들레가 1개이지만 삭제된 경우")
    void countUsingSeedWhenPendingAndDeleted() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.PENDING);
        dandelion1.delete();
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(0);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - blossomed 상태의 민들레가 1개인 경우")
    void countUsingSeedWhenBlossomed() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.BLOSSOMED);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(1);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - blossomed 상태의 민들레가 1개이지만 삭제된 경우")
    void countUsingSeedWhenBlossomedAndDeleted() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.BLOSSOMED);
        dandelion1.delete();
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(0);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - blocked 상태의 민들레만 1개일 경우")
    void countUsingSeedWhenBlocked() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.BLOCKED);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(0);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - album 상태의 민들레만 1개일 경우")
    void countUsingSeedWhenAlbum() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.ALBUM);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(0);
    }

    @Test
    @DisplayName("꽃밭 민들레 리스트 조회 - 조회 성공")
    void getDandelionListInTheGarden() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(2)
                .member(member1)
                .build();
        dandelion3 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(3)
                .member(member1)
                .build();
        dandelion2.changeStatus(Dandelion.Status.HOLD);
        dandelion3.changeStatus(Dandelion.Status.BLOSSOMED);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        Dandelion savedDandelion3 = dandelionRepository.save(dandelion3);
        em.flush();
        em.clear();

        // when
        List<Dandelion> dandelions = dandelionRepository.findActiveDandelionListByMemberSeq(savedMember.getSeq());
        // then
        assertThat(dandelions.size()).isEqualTo(3);
        assertThat(dandelions.get(0).getStatus()).isEqualTo(Dandelion.Status.FLYING);
        assertThat(dandelions.get(1).getStatus()).isEqualTo(Dandelion.Status.HOLD);
        assertThat(dandelions.get(2).getStatus()).isEqualTo(Dandelion.Status.BLOSSOMED);
        assertThat(dandelions.get(0).getSeq()).isEqualTo(savedDandelion1.getSeq());
        assertThat(dandelions.get(1).getSeq()).isEqualTo(savedDandelion2.getSeq());
        assertThat(dandelions.get(2).getSeq()).isEqualTo(savedDandelion3.getSeq());
    }

    @Test
    @DisplayName("꽃밭 민들레 리스트 조회 - 삭제된 민들레 조회 시도")
    void getDandelionListInTheGardenIsDeleted() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(2)
                .member(member1)
                .build();
        dandelion1.delete();
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        // when
        List<Dandelion> dandelions = dandelionRepository.findActiveDandelionListByMemberSeq(savedMember.getSeq());

        // then
        assertThat(dandelions.size()).isEqualTo(1);
        assertThat(dandelions.get(0).getStatus()).isEqualTo(Dandelion.Status.FLYING);
        assertThat(dandelions.get(0).getSeq()).isEqualTo(savedDandelion2.getSeq());
    }

    @Test
    @DisplayName("꽃밭 민들레 리스트 조회 - album 상태의 민들레 조회 시도")
    void getDandelionListInTheGardenAlbum() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(2)
                .member(member1)
                .build();
        dandelion1.changeStatus(Dandelion.Status.ALBUM);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        // when
        List<Dandelion> dandelions = dandelionRepository.findActiveDandelionListByMemberSeq(savedMember.getSeq());
        // then
        assertThat(dandelions.size()).isEqualTo(1);
        assertThat(dandelions.get(0).getStatus()).isEqualTo(Dandelion.Status.FLYING);
        assertThat(dandelions.get(0).getSeq()).isEqualTo(savedDandelion2.getSeq());
    }

    @Test
    @DisplayName("꽃밭 민들레 리스트 조회 - block 상태의 민들레 조회 시도")
    void getDandelionListInTheGardenBlock() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(2)
                .member(member1)
                .build();
        dandelion1.changeStatus(Dandelion.Status.BLOCKED);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        // when
        List<Dandelion> dandelions = dandelionRepository.findActiveDandelionListByMemberSeq(savedMember.getSeq());
        // then
        assertThat(dandelions.size()).isEqualTo(1);
        assertThat(dandelions.get(0).getStatus()).isEqualTo(Dandelion.Status.FLYING);
        assertThat(dandelions.get(0).getSeq()).isEqualTo(savedDandelion2.getSeq());
    }

    @Test
    @DisplayName("꽃밭 민들레 리스트 조회 - 삭제된 유저의 민들레 조회 시도")
    void getDandelionListInTheGardenMemberIsDeleted() {
        // given
        member1.delete();
        Member savedMember = memberRepository.save(member1);
        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(2)
                .member(member1)
                .build();
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        // when
        List<Dandelion> dandelions = dandelionRepository.findActiveDandelionListByMemberSeq(savedMember.getSeq());
        // then
        assertThat(dandelions.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("특정 회원의 민들레를 제외하고 민들레 데이터 랜덤 조회 - 성공")
    void findRandomDandelionExceptMemberSuccess() {
        // given
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Member member = Member.builder()
                    .id("아이디" + i)
                    .password("패스워드" + i)
                    .build();
            members.add(member);
            memberRepository.save(member);

            appConfigRepository.save(AppConfig.builder()
                    .member(member)
                    .language(AppConfig.Language.ENGLISH)
                    .build());

            dandelionRepository.save(Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-04-2" + i))
                    .community(Community.KOREA)
                    .flowerSignNumber(1)
                    .member(member)
                    .build());
        }

        em.flush();
        em.clear();

        // when
        Dandelion findRandomDandelion = dandelionRepository.findRandomFlyingDandelionExceptMember(members.get(0))
                .orElse(null);

        // then
        assertThat(findRandomDandelion.getSeq()).isEqualTo(members.get(1).getDandelions().get(0).getSeq());
    }

    @Test
    @DisplayName("특정 회원의 민들레를 제외하고 민들레 데이터 랜덤 조회 - 성공 : 이미 잡은 민들레가 있을 경우")
    void findRandomDandelionExceptMemberWhenCatchOneBefore() {
        // given
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Member member = Member.builder()
                    .id("아이디" + i)
                    .password("패스워드" + i)
                    .build();
            members.add(member);
            memberRepository.save(member);

            appConfigRepository.save(AppConfig.builder()
                    .member(member)
                    .language(AppConfig.Language.ENGLISH)
                    .build());

            dandelionRepository.save(Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-04-2" + i))
                    .community(Community.KOREA)
                    .flowerSignNumber(1)
                    .member(member)
                    .build());
        }

        em.persist(MemberDandelionHistory.builder()
                .member(members.get(0))
                .dandelion(members.get(1).getDandelions().get(0))
                .build());

        em.flush();
        em.clear();

        // when
        Dandelion findRandomDandelion = dandelionRepository.findRandomFlyingDandelionExceptMember(members.get(0))
                .orElse(null);

        // then
        assertThat(findRandomDandelion.getSeq()).isEqualTo(members.get(2).getDandelions().get(0).getSeq());
    }

    @Test
    @DisplayName("특정 회원의 민들레를 제외하고 민들레 데이터 랜덤 조회 - 성공 : 이미 잡은 민들레가 있고 중간에 HOLD 인 민들레가 1개 있을 경우")
    void findRandomDandelionExceptMemberWhenCatchOneBeforeAndHoldOne() {
        // given
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Member member = Member.builder()
                    .id("아이디" + i)
                    .password("패스워드" + i)
                    .build();
            members.add(member);
            memberRepository.save(member);

            appConfigRepository.save(AppConfig.builder()
                    .member(member)
                    .language(AppConfig.Language.ENGLISH)
                    .build());

            Dandelion savedDandelion = dandelionRepository.save(Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-04-2" + i))
                    .community(Community.KOREA)
                    .flowerSignNumber(1)
                    .member(member)
                    .build());
            if (i == 3) {
                savedDandelion.changeStatus(Dandelion.Status.HOLD);
            }
        }

        em.persist(MemberDandelionHistory.builder()
                .member(members.get(0))
                .dandelion(members.get(1).getDandelions().get(0))
                .build());

        em.flush();
        em.clear();

        // when
        Dandelion findRandomDandelion = dandelionRepository.findRandomFlyingDandelionExceptMember(members.get(0))
                .orElse(null);

        // then
        assertThat(findRandomDandelion.getSeq()).isEqualTo(members.get(3).getDandelions().get(0).getSeq());
    }

    @Test
    @DisplayName("특정 회원의 민들레를 제외하고 민들레 데이터 랜덤 조회 - 민들레 데이터가 없는 경우")
    void findRandomDandelionExceptMemberWhenNotExistDandelion() {
        // given
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Member member = Member.builder()
                    .id("아이디" + i)
                    .password("패스워드" + i)
                    .build();
            members.add(member);
            memberRepository.save(member);

            appConfigRepository.save(AppConfig.builder()
                    .member(member)
                    .language(AppConfig.Language.ENGLISH)
                    .build());
        }

        em.flush();
        em.clear();

        // when
        Dandelion findRandomDandelion = dandelionRepository.findRandomFlyingDandelionExceptMember(members.get(0))
                .orElse(null);

        // then
        assertThat(findRandomDandelion).isNull();
    }

    @Test
    @DisplayName("특정 회원의 민들레를 제외하고 민들레 데이터 랜덤 조회 - 모든 민들레가 삭제된 경우")
    void findRandomDandelionExceptMemberWhenAllDandelionDeleted() {
        // given
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Member member = Member.builder()
                    .id("아이디" + i)
                    .password("패스워드" + i)
                    .build();
            members.add(member);
            memberRepository.save(member);

            appConfigRepository.save(AppConfig.builder()
                    .member(member)
                    .language(AppConfig.Language.ENGLISH)
                    .build());

            Dandelion newDandelion = Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-04-2" + i))
                    .community(Community.KOREA)
                    .flowerSignNumber(1)
                    .member(member)
                    .build();

            newDandelion.delete();
            dandelionRepository.save(newDandelion);
        }

        em.flush();
        em.clear();

        // when
        Dandelion findRandomDandelion = dandelionRepository.findRandomFlyingDandelionExceptMember(members.get(0))
                .orElse(null);

        // then
        assertThat(findRandomDandelion).isNull();
    }

    @Test
    @DisplayName("특정 회원의 민들레를 제외하고 민들레 데이터 랜덤 조회 - 상태가 FLYING 인 민들레가 없는 경우")
    void findRandomDandelionExceptMemberWhenAllDandelionNoFlying() {
        // given
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Member member = Member.builder()
                    .id("아이디" + i)
                    .password("패스워드" + i)
                    .build();
            members.add(member);
            memberRepository.save(member);

            appConfigRepository.save(AppConfig.builder()
                    .member(member)
                    .language(AppConfig.Language.ENGLISH)
                    .build());

            Dandelion newDandelion = Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-04-2" + i))
                    .community(Community.KOREA)
                    .flowerSignNumber(1)
                    .member(member)
                    .build();

            newDandelion.changeStatus(Dandelion.Status.HOLD);
            dandelionRepository.save(newDandelion);
        }

        em.flush();
        em.clear();

        // when
        Dandelion findRandomDandelion = dandelionRepository.findRandomFlyingDandelionExceptMember(members.get(0))
                .orElse(null);

        // then
        assertThat(findRandomDandelion).isNull();
    }

    @Test
    @DisplayName("특정 회원의 민들레를 제외하고 민들레 데이터 랜덤 조회 - 모든 민들레의 소유자가 요청한 사람인 경우")
    void findRandomDandelionExceptMemberWhenAllDandelionOwner() {
        // given
        Member newMember = Member.builder()
                .id("아이디1")
                .password("패스워드1")
                .build();
        memberRepository.save(newMember);
        appConfigRepository.save(AppConfig.builder()
                .member(newMember)
                .language(AppConfig.Language.ENGLISH)
                .build());
        for (int i = 1; i <= 5; i++) {
            Dandelion newDandelion = Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-04-2" + i))
                    .community(Community.KOREA)
                    .flowerSignNumber(1)
                    .member(newMember)
                    .build();

            dandelionRepository.save(newDandelion);
        }

        em.flush();
        em.clear();

        // when
        Dandelion findRandomDandelion = dandelionRepository.findRandomFlyingDandelionExceptMember(newMember)
                .orElse(null);

        // then
        assertThat(findRandomDandelion).isNull();
    }

    @Test
    @DisplayName("특정 회원의 민들레를 제외하고 민들레 데이터 랜덤 조회 - 모든 민들레를 잡아본 경우")
    void findRandomDandelionExceptMemberWhenAllDandelionCatch() {
        // given
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Member member = Member.builder()
                    .id("아이디" + i)
                    .password("패스워드" + i)
                    .build();
            members.add(member);
            memberRepository.save(member);

            appConfigRepository.save(AppConfig.builder()
                    .member(member)
                    .language(AppConfig.Language.ENGLISH)
                    .build());

            Dandelion newDandelion = Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-04-2" + i))
                    .community(Community.KOREA)
                    .flowerSignNumber(1)
                    .member(member)
                    .build();

            dandelionRepository.save(newDandelion);
        }

        for (int i = 1; i < 5; i++) {
            em.persist(MemberDandelionHistory.builder()
                    .member(members.get(0))
                    .dandelion(members.get(i).getDandelions().get(0))
                    .build());
        }

        em.flush();
        em.clear();

        // when
        Dandelion findRandomDandelion = dandelionRepository.findRandomFlyingDandelionExceptMember(members.get(0))
                .orElse(null);

        // then
        assertThat(findRandomDandelion).isNull();
    }

    @Test
    @DisplayName("경과 시간이 지난 HOLD 상태인 민들레를 FLYING 상태로 업데이트 - 성공")
    void updateHoldingDandelionToFlying() throws InterruptedException {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);

        dandelionRepository.save(dandelion1);

        dandelion2.changeStatus(Dandelion.Status.HOLD);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        // when
        Thread.sleep(65000);
        dandelionRepository.updateHoldingDandelionToFlying(1L);
        Dandelion findDandelion2 = dandelionRepository.findBySeq(savedDandelion2.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion2.getStatus()).isEqualTo(Dandelion.Status.FLYING);
    }

    @Test
    @DisplayName("경과 시간이 지난 HOLD 상태인 민드렐를 FLYING 상태로 업데이트 - 경과 시간이 지나지 않은 경우")
    void updateHoldingDandelionToFlyingWhenBeforeTime() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);

        dandelionRepository.save(dandelion1);

        dandelion2.changeStatus(Dandelion.Status.HOLD);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        // when
        dandelionRepository.updateHoldingDandelionToFlying(1L);
        Dandelion findDandelion2 = dandelionRepository.findBySeq(savedDandelion2.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion2.getStatus()).isEqualTo(Dandelion.Status.HOLD);
    }

    @Test
    @DisplayName("경과 시간이 지난 HOLD 상태인 민들레를 FLYING 상태로 업데이트 - 민들레가 경과 시간이 지났는데 Deleted 된 경우")
    void updateHoldingDandelionToFlyingWhenDandelionDeleted() throws InterruptedException {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);

        dandelionRepository.save(dandelion1);

        dandelion2.changeStatus(Dandelion.Status.HOLD);
        dandelion2.delete();
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        // when
        Thread.sleep(65000);
        dandelionRepository.updateHoldingDandelionToFlying(1L);
        Dandelion findDandelion2 = dandelionRepository.findBySeqContainsDeleted(savedDandelion2.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion2.getStatus()).isEqualTo(Dandelion.Status.HOLD);
    }

    @Test
    @DisplayName("민들레 식별키로 민들레 엔티티(삭제 포함) 조회 - 데이터가 없는 경우")
    void findDandelionBySeqContainsDeleted() {
        // given


        // when
        Dandelion findDandelion = dandelionRepository.findBySeqContainsDeleted(0L)
                .orElse(null);

        // then
        assertThat(findDandelion).isNull();
    }

    @Test
    @DisplayName("민들레 식별키로 민들레 엔티티(삭제 포함) 조회 - 데이터가 있는 경우")
    void findDandelionBySeqContainsDeletedWhenExist() {
        // given
        memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        Dandelion findDandelion = dandelionRepository.findBySeqContainsDeleted(savedDandelion1.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion.getSeq()).isEqualTo(savedDandelion1.getSeq());
        assertThat(findDandelion.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("민들레 식별키로 민들레 엔티티(삭제 포함) 조회 - 데이터가 있고 삭제 처리된 경우")
    void findDandelionBySeqContainsDeletedWhenExistAndDeleted() {
        // given
        memberRepository.save(member1);
        dandelion1.delete();
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        Dandelion findDandelion = dandelionRepository.findBySeqContainsDeleted(savedDandelion1.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion.getSeq()).isEqualTo(savedDandelion1.getSeq());
        assertThat(findDandelion.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("꽃밭 앨범 페이지 조회 - 데이터가 있는 경우(첫번째 페이지)")
    void getAlbumFirstPageWhenExistData() {
        // given
        memberRepository.save(member1);
        List<Dandelion> dandelions = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            Dandelion dandelion = Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-04-1" + String.valueOf(i % 10)))
                    .community(member1.getAppConfig().getCommunity())
                    .flowerSignNumber(1)
                    .member(member1)
                    .build();
            if (i < 10) {
                dandelion.changeStatus(Dandelion.Status.ALBUM);
            }
            dandelion.changeDescription(String.valueOf(i));
            dandelions.add(dandelion);
            dandelionRepository.save(dandelion);
        }
        em.flush();
        em.clear();

        // when
        int page = 0;
        int size = 3;
        Page<Dandelion> dandelionPage = dandelionRepository.findAlbumByMemberSeq(member1.getSeq(), PageRequest.of(page, size));

        // then
        assertThat(dandelionPage.getTotalElements()).isEqualTo(10); // 총 데이터 개수
        assertThat(dandelionPage.getTotalPages()).isEqualTo(4); // 총 페이지 개수
        assertThat(dandelionPage.getNumber()).isEqualTo(0);  // 현재 페이지 수
        assertThat(dandelionPage.getNumberOfElements()).isEqualTo(3); // 현재 페이지의 데이터 개수
        assertThat(dandelionPage.getContent().get(0).getDescription()).isEqualTo("9");
        assertThat(dandelionPage.getContent().get(1).getDescription()).isEqualTo("8");
    }

    @Test
    @DisplayName("꽃밭 앨범 페이지 조회 - 데이터가 있는 경우(마지막 페이지)")
    void getAlbumLastPageWhenExistData() {
        // given
        memberRepository.save(member1);
        List<Dandelion> dandelions = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            Dandelion dandelion = Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-04-1" + String.valueOf(i % 10)))
                    .community(member1.getAppConfig().getCommunity())
                    .flowerSignNumber(1)
                    .member(member1)
                    .build();
            if (i < 10) {
                dandelion.changeStatus(Dandelion.Status.ALBUM);
            }
            dandelion.changeDescription(String.valueOf(9 - i));
            dandelions.add(dandelion);
            dandelionRepository.save(dandelion);
        }
        em.flush();
        em.clear();

        // when
        int page = 3;
        int size = 3;
        Page<Dandelion> dandelionPage = dandelionRepository.findAlbumByMemberSeq(member1.getSeq(), PageRequest.of(page, size));

        // then
        assertThat(dandelionPage.getTotalElements()).isEqualTo(10); // 총 데이터 개수
        assertThat(dandelionPage.getTotalPages()).isEqualTo(4); // 총 페이지 개수
        assertThat(dandelionPage.getNumber()).isEqualTo(3);  // 현재 페이지 수
        assertThat(dandelionPage.getNumberOfElements()).isEqualTo(1); // 현재 페이지의 데이터 개수
        assertThat(dandelionPage.getContent().get(0).getDescription()).isEqualTo("9");
    }

    @Test
    @DisplayName("꽃밭 앨범 페이지 조회 - 데이터가 없는경우")
    void getAlbumPageWhenNotExistData() {
        // given
        memberRepository.save(member1);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int page = 0;
        int size = 1;
        Page<Dandelion> dandelionPage = dandelionRepository.findAlbumByMemberSeq(member1.getSeq(), PageRequest.of(page, size));

        // then
        assertThat(dandelionPage.getTotalElements()).isEqualTo(0); // 총 데이터 개수
    }

    @Test
    @DisplayName("꽃밭 앨범 페이지 조회 - 없는 회원일 경우")
    void getAlbumPageWhenNotExistMember() {
        // given

        // when
        int page = 0;
        int size = 1;
        Page<Dandelion> dandelionPage = dandelionRepository.findAlbumByMemberSeq(0L, PageRequest.of(page, size));

        // then
        assertThat(dandelionPage.getTotalElements()).isEqualTo(0); // 총 데이터 개수
    }

    @Test
    @DisplayName("민들레 식별키로 민들레 데이터 존재 확인 - 데이터가 있는 경우")
    void existsDandelionBySeq() {
        // given
        memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        // when
        boolean isExists = dandelionRepository.existsBySeq(savedDandelion1.getSeq());

        // then
        assertThat(isExists).isTrue();
    }

    @Test
    @DisplayName("민들레 식별키로 민들레 데이터 존재 확인 - 데이터가 있지만 삭제 처리된 경우 경우")
    void existsDandelionBySeqWhenDeleted() {
        // given
        memberRepository.save(member1);
        dandelion1.delete();
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        // when
        boolean isExists = dandelionRepository.existsBySeq(savedDandelion1.getSeq());

        // then
        assertThat(isExists).isFalse();
    }

    @Test
    @DisplayName("민들레 식별키로 민들레 데이터 존재 확인 - 데이터가 없는 경우")
    void existsDandelionBySeqWhenNotExistsDandelion() {
        // given

        // when
        boolean isExists = dandelionRepository.existsBySeq(0L);

        // then
        assertThat(isExists).isFalse();
    }

    @Test
    @DisplayName("전체 기록보관함 민들레 개수 가져오기 - 데이터 있는 경우")
    void countToTalParticipationHasData() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        em.flush();
        List<Dandelion> dandelions = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            Dandelion dandelion = Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-04-30"))
                    .community(member1.getAppConfig().getCommunity())
                    .flowerSignNumber(1)
                    .member(member1)
                    .build();
            if (i % 3 == 0) {
                dandelion.changeStatus(Dandelion.Status.ALBUM);
            }
            if (i % 3 == 1) {
                dandelion.changeStatus(Dandelion.Status.BLOSSOMED);
            }
            dandelions.add(dandelion);
            dandelionRepository.save(dandelion);
            em.flush();
        }

        for (int i = 0; i < 21; i++) {
            Petal petal = Petal.builder()
                    .message("메시지1")
                    .imageFilename("이미지1")
                    .nation("국가1")
                    .dandelion(dandelions.get(i))
                    .member(member2)
                    .build();
            petalRepository.save(petal);
            em.flush();
        }
        em.clear();

        // when then

        // blossomed 7개 album 7개
        Long count = dandelionRepository.countParticipationDandelions(member2.getSeq());
        assertThat(count).isEqualTo(14L);

        // blossomed 7개 album 6개
        dandelions.get(3).delete();
        dandelionRepository.save(dandelions.get(3));
        em.flush();
        em.clear();

        count = dandelionRepository.countParticipationDandelions(member2.getSeq());
        assertThat(count).isEqualTo(13L);

        // blossomed 7개 album 5개
        dandelions.get(6).getPetals().get(0).delete();
        dandelionRepository.save(dandelions.get(6));
        em.flush();
        em.clear();

        count = dandelionRepository.countParticipationDandelions(member2.getSeq());
        assertThat(count).isEqualTo(12L);

        // 둘다 0개
        member1.delete();
        memberRepository.save(member1);
        em.flush();
        em.clear();

        count = dandelionRepository.countParticipationDandelions(member2.getSeq());
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @DisplayName("전체 기록보관함 민들레 개수 가져오기 - 데이터 없는 경우")
    void countToTalParticipationNoneData() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        em.flush();
        List<Dandelion> dandelions = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            Dandelion dandelion = Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-04-30"))
                    .community(member1.getAppConfig().getCommunity())
                    .flowerSignNumber(1)
                    .member(member1)
                    .build();
            if (i % 3 == 0) {
                dandelion.changeStatus(Dandelion.Status.ALBUM);
            }
            if (i % 3 == 1) {
                dandelion.changeStatus(Dandelion.Status.BLOSSOMED);
            }
            dandelions.add(dandelion);
            dandelionRepository.save(dandelion);
            em.flush();
        }
        em.clear();

        // when
        Long count = dandelionRepository.countParticipationDandelions(member2.getSeq());

        //then
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @DisplayName("요청 페이지의 민들레 리스트 가져오기 - 데이터가 있는경우")
    void getParticipationPageHasData() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        em.flush();
        List<Dandelion> dandelions = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            Dandelion dandelion = Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-04-" + (30 - i)))
                    .community(member1.getAppConfig().getCommunity())
                    .flowerSignNumber(i)
                    .member(member1)
                    .build();
            if (i % 3 == 0) {
                dandelion.changeStatus(Dandelion.Status.ALBUM);
            }
            if (i % 3 == 1) {
                dandelion.changeStatus(Dandelion.Status.BLOSSOMED);
            }
            dandelions.add(dandelion);
            dandelionRepository.save(dandelion);
            em.flush();
        }

        for (int i = 0; i < 21; i++) {
            Petal petal = Petal.builder()
                    .message("메시지1")
                    .imageFilename("이미지1")
                    .nation("국가1")
                    .dandelion(dandelions.get(i))
                    .member(member2)
                    .build();
            petalRepository.save(petal);
            em.flush();
        }

        // 첫번째 페이지
        int page = 1;
        int size = 3;
        List<Dandelion> newDandelions = dandelionRepository.findParticipationByMemberSeqAndPageable(member2.getSeq(), PageRequest.of(page, size))
                .orElse(null);

        assertThat(newDandelions.size()).isEqualTo(3);
        assertThat(newDandelions.get(0).getFlowerSignNumber()).isEqualTo(0);
        assertThat(newDandelions.get(1).getFlowerSignNumber()).isEqualTo(1);
        assertThat(newDandelions.get(2).getFlowerSignNumber()).isEqualTo(3);

        // 마지막 페이지
        page = 5;
        size = 3;
        newDandelions = dandelionRepository.findParticipationByMemberSeqAndPageable(member2.getSeq(), PageRequest.of(page, size))
                .orElse(null);
        assertThat(newDandelions.size()).isEqualTo(2);
        assertThat(newDandelions.get(0).getFlowerSignNumber()).isEqualTo(18);
        assertThat(newDandelions.get(1).getFlowerSignNumber()).isEqualTo(19);

        // 0번 민들레 삭제
        dandelions.get(0).delete();
        dandelionRepository.save(dandelions.get(0));
        em.flush();
        em.clear();

        page = 1;
        size = 3;
        newDandelions = dandelionRepository.findParticipationByMemberSeqAndPageable(member2.getSeq(), PageRequest.of(page, size))
                .orElse(null);

        assertThat(newDandelions.size()).isEqualTo(3);
        assertThat(newDandelions.get(0).getFlowerSignNumber()).isEqualTo(1);
        assertThat(newDandelions.get(1).getFlowerSignNumber()).isEqualTo(3);
        assertThat(newDandelions.get(2).getFlowerSignNumber()).isEqualTo(4);

        // 3번 민들레 꽃잎 삭제
        dandelions.get(3).getPetals().get(0).delete();
        petalRepository.save(dandelions.get(3).getPetals().get(0));
        dandelionRepository.save(dandelions.get(3));
        em.flush();
        em.clear();

        page = 1;
        size = 3;
        newDandelions = dandelionRepository.findParticipationByMemberSeqAndPageable(member2.getSeq(), PageRequest.of(page, size))
                .orElse(null);

        assertThat(newDandelions.size()).isEqualTo(3);
        assertThat(newDandelions.get(0).getFlowerSignNumber()).isEqualTo(1);
        assertThat(newDandelions.get(1).getFlowerSignNumber()).isEqualTo(4);
        assertThat(newDandelions.get(2).getFlowerSignNumber()).isEqualTo(6);

        // 민들레 주인 탈퇴
        member1.delete();
        memberRepository.save(member1);
        em.flush();
        em.clear();

        page = 1;
        size = 3;
        newDandelions = dandelionRepository.findParticipationByMemberSeqAndPageable(member2.getSeq(), PageRequest.of(page, size))
                .orElse(null);

        assertThat(newDandelions).isNull();
    }

    @Test
    @DisplayName("요청 페이지의 민들레 리스트 가져오기 - 데이터가 없는경우")
    void getParticipationPageNoneData() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        em.flush();
        em.clear();

        // when
        int page = 1;
        int size = 3;
        List<Dandelion> newDandelions = dandelionRepository.findParticipationByMemberSeqAndPageable(member2.getSeq(), PageRequest.of(page, size))
                .orElse(null);

        // then
        assertThat(newDandelions).isNull();
    }

    @Test
    @DisplayName("민들레 READY 상태로 변경 - 성공(From FLYING)")
    void updateDandelionStatusToReadyFromFlying() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.now().plusDays(1L))
                .community(member2.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.FLYING);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        dandelionRepository.updateFlyingOrHoldingDandelionToReady();
        Dandelion findDandelion = dandelionRepository.findBySeq(savedDandelion.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion.getSeq()).isEqualTo(savedDandelion.getSeq());
        assertThat(findDandelion.getMember().getSeq()).isEqualTo(savedDandelion.getMember().getSeq());
        assertThat(findDandelion.getStatus()).isEqualTo(Dandelion.Status.READY);
    }

    @Test
    @DisplayName("민들레 READY 상태로 변경 - 성공(From HOLD)")
    void updateDandelionStatusToReadyFromHold() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.now().plusDays(1L))
                .community(member2.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.HOLD);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        dandelionRepository.updateFlyingOrHoldingDandelionToReady();
        Dandelion findDandelion = dandelionRepository.findBySeq(savedDandelion.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion.getSeq()).isEqualTo(savedDandelion.getSeq());
        assertThat(findDandelion.getMember().getSeq()).isEqualTo(savedDandelion.getMember().getSeq());
        assertThat(findDandelion.getStatus()).isEqualTo(Dandelion.Status.READY);
    }

    @Test
    @DisplayName("민들레 READY 상태로 변경 - 변화 없음(From FLYING And Deleted)")
    void updateDandelionStatusToReadyFromFlyingAndDeleted() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.now().plusDays(1L))
                .community(member2.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.delete();
        newDandelion.changeStatus(Dandelion.Status.FLYING);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        dandelionRepository.updateFlyingOrHoldingDandelionToReady();
        Dandelion findDandelion = dandelionRepository.findBySeqContainsDeleted(savedDandelion.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion.getSeq()).isEqualTo(savedDandelion.getSeq());
        assertThat(findDandelion.getMember().getSeq()).isEqualTo(savedDandelion.getMember().getSeq());
        assertThat(findDandelion.getStatus()).isEqualTo(savedDandelion.getStatus());
    }

    @Test
    @DisplayName("민들레 READY 상태로 변경 - 변화 없음(From HOLD And Deleted)")
    void updateDandelionStatusToReadyFromHoldAndDeleted() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.now().plusDays(1L))
                .community(member2.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.delete();
        newDandelion.changeStatus(Dandelion.Status.HOLD);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        dandelionRepository.updateFlyingOrHoldingDandelionToReady();
        Dandelion findDandelion = dandelionRepository.findBySeqContainsDeleted(savedDandelion.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion.getSeq()).isEqualTo(savedDandelion.getSeq());
        assertThat(findDandelion.getMember().getSeq()).isEqualTo(savedDandelion.getMember().getSeq());
        assertThat(findDandelion.getStatus()).isEqualTo(savedDandelion.getStatus());
    }

    @Test
    @DisplayName("민들레 READY 상태로 변경 - 변화 없음(Not Yet Blossomed)")
    void updateDandelionStatusToReadyNotYetBlossomed() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.now().plusDays(2L))
                .community(member2.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.FLYING);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        dandelionRepository.updateFlyingOrHoldingDandelionToReady();
        Dandelion findDandelion = dandelionRepository.findBySeq(savedDandelion.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion.getSeq()).isEqualTo(savedDandelion.getSeq());
        assertThat(findDandelion.getMember().getSeq()).isEqualTo(savedDandelion.getMember().getSeq());
        assertThat(findDandelion.getStatus()).isEqualTo(savedDandelion.getStatus());
    }

    @Test
    @DisplayName("Ready상태의 민들레를 Return으로 바꾸기 - 성공")
    void changeDandelionStatusReadyToReturn() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelion1.changeStatus(Dandelion.Status.READY);
        dandelionRepository.save(dandelion1);
        dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        // when
        dandelionRepository.updateReadyDandelionToReturn();
        Dandelion newDandelion1 = dandelionRepository.findBySeq(dandelion1.getSeq()).orElse(null);
        Dandelion newDandelion2 = dandelionRepository.findBySeq(dandelion2.getSeq()).orElse(null);

        // then
        assertThat(newDandelion1.getStatus()).isEqualTo(Dandelion.Status.RETURN);
        assertThat(newDandelion2.getStatus()).isEqualTo(Dandelion.Status.FLYING);
    }

    @Test
    @DisplayName("Ready상태의 민들레를 Return으로 바꾸기 - 삭제된 민들레의 경우")
    void changeDandelionStatusReadyToReturnIsDeleted() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelion1.changeStatus(Dandelion.Status.READY);
        dandelion1.delete();
        dandelionRepository.save(dandelion1);
        dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        // when
        dandelionRepository.updateReadyDandelionToReturn();
        Dandelion newDandelion1 = dandelionRepository.findBySeqContainsDeleted(dandelion1.getSeq()).orElse(null);
        Dandelion newDandelion2 = dandelionRepository.findBySeq(dandelion2.getSeq()).orElse(null);

        // then
        assertThat(newDandelion1.getStatus()).isEqualTo(Dandelion.Status.READY);
        assertThat(newDandelion2.getStatus()).isEqualTo(Dandelion.Status.FLYING);
    }
}