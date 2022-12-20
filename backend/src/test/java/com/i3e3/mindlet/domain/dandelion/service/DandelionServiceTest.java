package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Petal;
import com.i3e3.mindlet.domain.dandelion.entity.Tag;
import com.i3e3.mindlet.domain.dandelion.repository.DandelionRepository;
import com.i3e3.mindlet.domain.dandelion.repository.PetalRepository;
import com.i3e3.mindlet.domain.dandelion.repository.TagRepository;
import com.i3e3.mindlet.domain.dandelion.service.dto.*;
import com.i3e3.mindlet.domain.member.entity.AppConfig;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.entity.MemberDandelionHistory;
import com.i3e3.mindlet.domain.member.repository.MemberDandelionHistoryRepository;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.constant.dandelion.DandelionConst;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import com.i3e3.mindlet.global.enums.Community;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class DandelionServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DandelionRepository dandelionRepository;

    @Autowired
    private DandelionService dandelionService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PetalRepository petalRepository;

    @Autowired
    private MemberDandelionHistoryRepository memberDandelionHistoryRepository;

    @Value("${path.access}")
    private String fileStorageUrl;

    @Value("${path.access.files.images.content}")
    private String contentImagePath;

    @Value("${path.access.files.images.nation}")
    private String nationImagePath;


    private Member member1, member2, member3;

    private AppConfig appConfig1, appConfig2, appConfig3;

    private Dandelion dandelion1, dandelion2, dandelion3;

    private Tag tag1;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        dandelionRepository.deleteAll();
        tagRepository.deleteAll();

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
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(member2.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(member2)
                .build();

        dandelion3 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(member3.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(member3)
                .build();
    }

    @Test
    @DisplayName("민들레 상태(Blossomed) 확인 - True")
    void checkBlossomedTrue() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.BLOSSOMED);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isBlossomed = dandelionService.isBlossomed(savedDandelion.getSeq());

        // then
        assertThat(isBlossomed).isTrue();
    }

    @Test
    @DisplayName("민들레 상태(Blossomed) 확인 - False")
    void checkBlossomedFalse() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.FLYING);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isBlossomed = dandelionService.isBlossomed(savedDandelion.getSeq());

        // then
        assertThat(isBlossomed).isFalse();
    }

    @Test
    @DisplayName("민들레 상태(Blossomed) 확인 - 예외 발생")
    void checkBlossomedException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isBlossomed(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 주인 확인 - 주인이 맞는 경우")
    void checkOwnerTrue() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        boolean isOwner = dandelionService.isOwner(savedDandelion1.getSeq(), savedMember1.getSeq());

        // then
        assertThat(isOwner).isTrue();
    }

    @Test
    @DisplayName("민들레 주인 확인 - 주인이 아닌 경우")
    void checkOwnerFalse() {
        // given
        memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Member savedMember2 = memberRepository.save(member2);
        dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        // when
        boolean isOwner = dandelionService.isOwner(savedDandelion1.getSeq(), savedMember2.getSeq());

        // then
        assertThat(isOwner).isFalse();
    }

    @Test
    @DisplayName("민들레 주인 확인 - 예외 발생")
    void checkOwnerException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isOwner(0L, 0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 꽃말 수정 - 성공")
    void changeDescriptionSuccess() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        String newDescription = savedDandelion1.getDescription() + "1";

        // when
        String findDescription = dandelionService.changeDescription(savedDandelion1.getSeq(), newDescription);

        // then
        assertThat(findDescription).isEqualTo(newDescription);
        assertThat(findDescription).isNotEqualTo(savedDandelion1.getDescription());
    }

    @Test
    @DisplayName("민들레 꽃말 수정 - 예외 발생")
    void changeDescriptionException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.changeDescription(0L, "꽃말"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - 예외 발생: 해당 회원이 존재하지 않는 경우")
    void countLeftSeedWhenNotExistMember() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.getLeftSeedCount(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - 민들레가 1개도 등록되지 않은 경우")
    void countLeftSeedWhenNotExistDandelion() {
        // given
        member1.getDandelions().clear();
        dandelion1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - flying 상태의 민들레가 1개 등록될 경우")
    void countLeftSeedWhenFlying() {
        // given
        dandelion1.changeStatus(Dandelion.Status.FLYING);
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue() - 1);
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - flying 상태의 민들레가 1개 있지만 삭제된 경우")
    void countLeftSeedWhenFlyingAndDeleted() {
        // given
        dandelion1.changeStatus(Dandelion.Status.FLYING);
        dandelion1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - hold 상태의 민들레가 1개 등록될 경우")
    void countLeftSeedWhenHold() {
        // given
        dandelion1.changeStatus(Dandelion.Status.HOLD);
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue() - 1);
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - hold 상태의 민들레가 1개 있지만 삭제된 경우")
    void countLeftSeedWhenHoldAndDeleted() {
        // given
        dandelion1.changeStatus(Dandelion.Status.HOLD);
        dandelion1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - return 상태의 민들레가 1개 등록될 경우")
    void countLeftSeedWhenReturn() {
        // given
        dandelion1.changeStatus(Dandelion.Status.RETURN);
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue() - 1);
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - return 상태의 민들레가 1개 있지만 삭제된 경우")
    void countLeftSeedWhenReturnAndDeleted() {
        // given
        dandelion1.changeStatus(Dandelion.Status.RETURN);
        dandelion1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - pending 상태의 민들레가 1개 등록될 경우")
    void countLeftSeedWhenPending() {
        // given
        dandelion1.changeStatus(Dandelion.Status.PENDING);
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue() - 1);
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - pending 상태의 민들레가 1개 있지만 삭제된 경우")
    void countLeftSeedWhenPendingAndDeleted() {
        // given
        dandelion1.changeStatus(Dandelion.Status.PENDING);
        dandelion1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - blossomed 상태의 민들레가 1개 등록될 경우")
    void countLeftSeedWhenBlossomed() {
        // given
        dandelion1.changeStatus(Dandelion.Status.BLOSSOMED);
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue() - 1);
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - blossomed 상태의 민들레가 1개 있지만 삭제된 경우")
    void countLeftSeedWhenBlossomedAndDeleted() {
        // given
        dandelion1.changeStatus(Dandelion.Status.BLOSSOMED);
        dandelion1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - blocked 상태의 민들레가 1개인 경우")
    void countLeftSeedWhenBlocked() {
        // given
        dandelion1.changeStatus(Dandelion.Status.BLOCKED);
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - album 상태의 민들레가 1개인 경우")
    void countLeftSeedWhenAlbum() {
        // given
        dandelion1.changeStatus(Dandelion.Status.ALBUM);
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - 한 회원이 6개의 민들레를 등록한 경우")
    void countLeftSeedWhenMoreThanMaxCount() {
        // given
        Member savedMember = memberRepository.save(Member.builder()
                .id("test1")
                .password("midlet")
                .build());

        for (int i = 1; i <= 6; i++) {
            dandelionRepository.save(Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-05-03"))
                    .community(Community.WORLD)
                    .flowerSignNumber(i)
                    .member(savedMember)
                    .build());
        }
        em.flush();
        em.clear();

        // when

        // then
        assertThatThrownBy(() -> dandelionService.getLeftSeedCount(savedMember.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.MORE_THAN_MAX_COUNT.getMessage());
    }

    @Test
    @DisplayName("민들레 상태(Return) 확인 - True")
    void checkReturnTrue() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-05-04"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.RETURN);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isReturn = dandelionService.isReturn(savedDandelion.getSeq());

        // then
        assertThat(isReturn).isTrue();
    }

    @Test
    @DisplayName("민들레 상태(Return) 확인 - False")
    void checkReturnFalse() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-05-04"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.FLYING);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isReturn = dandelionService.isReturn(savedDandelion.getSeq());

        // then
        assertThat(isReturn).isFalse();
    }

    @Test
    @DisplayName("민들레 상태(Return) 확인 - 예외 발생")
    void checkReturnException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isReturn(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 상태 변경")
    void changeDandelionStatus() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-05-04"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.RETURN);
        dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        dandelionService.changeStatus(newDandelion.getSeq(), Dandelion.Status.BLOSSOMED);
        Dandelion findDandelion = dandelionRepository.findBySeq(newDandelion.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion.getStatus()).isEqualTo(Dandelion.Status.BLOSSOMED);
    }

    @Test
    @DisplayName("민들레 상태 변경 - 예외 발생")
    void changeDandelionStatusException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.changeStatus(0L, Dandelion.Status.FLYING))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 태그 삭제 성공")
    void deleteDandelionTagSuccess() {
        // given
        memberRepository.save(member1);
        Member savedMember3 = memberRepository.save(member3);
        Dandelion savedDandelion = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        tag1 = Tag.builder()
                .dandelion(savedDandelion)
                .name("2022년 팬 미팅")
                .member(savedMember3)
                .build();

        Tag savedTag = tagRepository.save(tag1);

        em.flush();
        em.clear();

        // when
        dandelionService.deleteTag(savedTag.getSeq(), member3.getSeq());

        // then
        assertThat(tagRepository.findBySeq(savedTag.getSeq())).isEmpty();
    }

    @Test
    @DisplayName("민들레 태그 삭제 실패 - Tag 없음")
    void deleteDandelionTagFailNonTag() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.deleteTag(0L, 1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 태그 삭제 실패 - Seq 불 일치")
    void deleteDandelionTagFailNotEqualsMemberSeq() {
        // given
        memberRepository.save(member1);
        Member savedMember3 = memberRepository.save(member3);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        tag1 = Tag.builder()
                .dandelion(savedDandelion)
                .name("2022년 팬 미팅")
                .member(savedMember3)
                .build();

        Tag savedTag = tagRepository.save(tag1);

        em.flush();
        em.clear();

        // when

        // then
        assertThatThrownBy(() -> dandelionService.deleteTag(savedTag.getSeq(), savedMember2.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 삭제 성공")
    void deleteDandelionSuccess() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        Petal savedPetal1 = petalRepository.save(
                Petal.builder()
                        .member(savedMember1)
                        .dandelion(savedDandelion1)
                        .message("안녕하세요!!!")
                        .nation("Korea")
                        .build());

        em.flush();
        em.clear();


        Petal savedPetal2 = petalRepository.save(
                Petal.builder()
                        .member(savedMember2)
                        .dandelion(savedDandelion1)
                        .message("Nice to see ya")
                        .nation("CANADA")
                        .build());

        em.flush();
        em.clear();

        // when
        dandelionService.deleteDandelion(savedDandelion1.getSeq(), savedMember1.getSeq());

        // then
        assertThat(dandelionRepository.findBySeq(dandelion1.getSeq())).isEmpty();
        assertThat(petalRepository.findBySeq(savedPetal1.getSeq())).isEmpty();
        assertThat(petalRepository.findBySeq(savedPetal2.getSeq())).isEmpty();
    }

    @Test
    @DisplayName("민들레 삭제 실패 - 민들레 없음")
    void deleteDandelionFailNotExistDandelion() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        // when

        // then
        assertThatThrownBy(() -> dandelionService.deleteDandelion(0L, savedMember1.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 삭제 실패 - 회원 식별키와 불 일치")
    void deleteDandelionFailNotEqualMemberSeq() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        petalRepository.save(
                Petal.builder()
                        .member(savedMember1)
                        .dandelion(savedDandelion1)
                        .message("안녕하세요!!!")
                        .nation("Korea")
                        .build());

        em.flush();
        em.clear();

        // when


        // then
        assertThatThrownBy(() -> dandelionService.deleteDandelion(savedDandelion1.getSeq(), savedMember2.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 삭제 실패 - 회원이 deleted")
    void deleteDandelionFailMemberDeleted() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        savedMember1.delete();
        petalRepository.save(
                Petal.builder()
                        .member(savedMember1)
                        .dandelion(savedDandelion1)
                        .message("안녕하세요!!!")
                        .nation("Korea")
                        .build());

        em.flush();
        em.clear();

        // when


        // then
        assertThatThrownBy(() -> dandelionService.deleteDandelion(savedDandelion1.getSeq(), savedMember1.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("꽃밭 정보 반환 - 성공")
    void getGardenInfoListSuccess() {
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
                .flowerSignNumber(5)
                .member(member1)
                .build();
        dandelion2.changeStatus(Dandelion.Status.BLOSSOMED);
        dandelion3.changeStatus(Dandelion.Status.RETURN);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        Dandelion savedDandelion3 = dandelionRepository.save(dandelion3);
        em.flush();
        em.clear();

        // when
        List<ResponseGardenInfoDto> responseGardenInfos = dandelionService.getGardenInfoList(savedMember.getSeq());

        // then
        assertThat(responseGardenInfos.size()).isEqualTo(5);
        assertThat(responseGardenInfos.get(3)).isNull();
        assertThat(responseGardenInfos.get(0).getStatus()).isEqualTo("FLYING");
        assertThat(responseGardenInfos.get(1).getStatus()).isEqualTo("BLOSSOMED");
        assertThat(responseGardenInfos.get(4).getStatus()).isEqualTo("RETURN");
        assertThat(responseGardenInfos.get(0).getSeq()).isEqualTo(savedDandelion1.getSeq());
        assertThat(responseGardenInfos.get(1).getSeq()).isEqualTo(savedDandelion2.getSeq());
        assertThat(responseGardenInfos.get(4).getSeq()).isEqualTo(savedDandelion3.getSeq());
    }

    @Test
    @DisplayName("꽃밭 정보 반환 - 없는 회원 시도")
    void getGardenInfoListNoneMember() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.getGardenInfoList(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("꽃밭 정보 반환 - 삭제된 회원 시도")
    void getGardenInfoListMemberIsDeleted() {
        // given
        member1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when

        // then
        assertThatThrownBy(() -> dandelionService.getGardenInfoList(savedMember.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("꽃밭 정보 반환 - 삭제된 민들레 시도")
    void getGardenInfoListContainIsDeleted() {
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
        dandelion2.delete();
        dandelion3.delete();
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        Dandelion savedDandelion3 = dandelionRepository.save(dandelion3);
        em.flush();
        em.clear();

        // when
        List<ResponseGardenInfoDto> responseGardenInfos = dandelionService.getGardenInfoList(savedMember.getSeq());
        // then
        assertThat(responseGardenInfos.size()).isEqualTo(5);
        assertThat(responseGardenInfos.get(0).getStatus()).isEqualTo("FLYING");
        assertThat(responseGardenInfos.get(0).getSeq()).isEqualTo(savedDandelion1.getSeq());
    }

    @Test
    @DisplayName("꽃밭 정보 반환 - blocked 상태의 민들레 시도")
    void getGardenInfoListContainBlocked() {
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
        dandelion3.changeStatus(Dandelion.Status.BLOCKED);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        Dandelion savedDandelion3 = dandelionRepository.save(dandelion3);
        em.flush();
        em.clear();

        // when
        List<ResponseGardenInfoDto> responseGardenInfos = dandelionService.getGardenInfoList(savedMember.getSeq());

        // then
        assertThat(responseGardenInfos.size()).isEqualTo(5);
        assertThat(responseGardenInfos.get(0).getStatus()).isEqualTo("FLYING");
        assertThat(responseGardenInfos.get(1).getStatus()).isEqualTo("FLYING");
        assertThat(responseGardenInfos.get(0).getSeq()).isEqualTo(savedDandelion1.getSeq());
        assertThat(responseGardenInfos.get(1).getSeq()).isEqualTo(savedDandelion2.getSeq());
    }

    @Test
    @DisplayName("꽃밭 정보 반환 - album 상태의 민들레 시도")
    void getGardenInfoListContainAlbum() {
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
        dandelion3.changeStatus(Dandelion.Status.ALBUM);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        Dandelion savedDandelion3 = dandelionRepository.save(dandelion3);
        em.flush();
        em.clear();

        // when
        List<ResponseGardenInfoDto> responseGardenInfos = dandelionService.getGardenInfoList(savedMember.getSeq());

        // then
        assertThat(responseGardenInfos.size()).isEqualTo(5);
        assertThat(responseGardenInfos.get(0).getStatus()).isEqualTo("FLYING");
        assertThat(responseGardenInfos.get(1).getStatus()).isEqualTo("FLYING");
        assertThat(responseGardenInfos.get(0).getSeq()).isEqualTo(savedDandelion1.getSeq());
        assertThat(responseGardenInfos.get(1).getSeq()).isEqualTo(savedDandelion2.getSeq());
    }

    @Test
    @DisplayName("랜덤 민들레씨 정보 조회 - 성공")
    void findRandomDandelionSeed() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Member savedMember3 = memberRepository.save(member3);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        dandelionRepository.save(dandelion2);
        dandelionRepository.save(dandelion3);
        em.flush();
        em.clear();

        Petal savedPetal1 = petalRepository.save(Petal.builder()
                .message("메시지1")
                .imageFilename("이미지1")
                .nation("국가1")
                .dandelion(savedDandelion1)
                .member(savedMember1)
                .build());
        Petal savedPetal2 = petalRepository.save(Petal.builder()
                .message("메시지2")
                .imageFilename("이미지2")
                .nation("국가2")
                .dandelion(savedDandelion1)
                .member(savedMember2)
                .build());
        em.flush();
        em.clear();

        // when
        DandelionDetailSvcDto dandelionDetailSvcDto = dandelionService.getDandelionSeedDto(savedMember3.getSeq());
        Long findDandelionSeq = dandelionDetailSvcDto.getDandelionSeq();
        Dandelion findDandelion = dandelionRepository.findBySeq(findDandelionSeq)
                .orElse(null);
        MemberDandelionHistory findMemberDandelionHistory = memberDandelionHistoryRepository.findByMemberAndDandelion(savedMember3, savedDandelion1)
                .orElse(null);

        // then

        /**
         * DTO 민들레 식별키 검증
         */
        assertThat(dandelionDetailSvcDto.getDandelionSeq()).isEqualTo(savedDandelion1.getSeq());

        /**
         * 꽃잎 데이터 검증
         */
        List<DandelionDetailSvcDto.PetalInfo> petalInfos = dandelionDetailSvcDto.getPetalInfos();
        assertThat(petalInfos.size()).isEqualTo(2);

        DandelionDetailSvcDto.PetalInfo findPetal1 = petalInfos.get(0);
        assertThat(findPetal1.getSeq()).isEqualTo(savedPetal1.getSeq());
        assertThat(findPetal1.getMessage()).isEqualTo(savedPetal1.getMessage());
        assertThat(findPetal1.getNation()).isEqualTo(savedPetal1.getNation());
        assertThat(findPetal1.getContentImageUrlPath()).isEqualTo(getContentImagePath(savedPetal1.getImageFilename()));

        DandelionDetailSvcDto.PetalInfo findPetal2 = petalInfos.get(1);
        assertThat(findPetal2.getSeq()).isEqualTo(savedPetal2.getSeq());
        assertThat(findPetal2.getMessage()).isEqualTo(savedPetal2.getMessage());
        assertThat(findPetal2.getNation()).isEqualTo(savedPetal2.getNation());
        assertThat(findPetal2.getContentImageUrlPath()).isEqualTo(getContentImagePath(savedPetal2.getImageFilename()));

        /**
         * 민들레 데이터 검증
         */
        assertThat(findDandelion.getStatus()).isEqualTo(Dandelion.Status.HOLD);
        assertThat(findDandelion.getCommunity()).isEqualTo(savedMember3.getAppConfig().getCommunity());
        assertThat(findDandelion.getMember().getSeq()).isNotEqualTo(savedMember3.getSeq());

        /**
         * 민들레 조회 이력 데이터 검증
         */
        assertThat(findMemberDandelionHistory.getMember().getSeq()).isEqualTo(savedMember3.getSeq());
        assertThat(findMemberDandelionHistory.getDandelion().getSeq()).isEqualTo(savedDandelion1.getSeq());
    }

    @Test
    @DisplayName("랜덤 민들레씨 정보 조회 - 민들레 데이터가 없는 경우")
    void findRandomDandelionSeedWhenNotExistDandelion() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        DandelionDetailSvcDto dandelionDetailSvcDto = dandelionService.getDandelionSeedDto(savedMember1.getSeq());

        // then
        assertThat(dandelionDetailSvcDto).isNull();
    }

    @Test
    @DisplayName("랜덤 민들레씨 정보 조회 - 모든 민들레가 삭제된 경우")
    void findRandomDandelionSeedWhenAllDandelionDeleted() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        dandelion1.delete();
        dandelion2.delete();
        dandelion3.delete();
        dandelionRepository.save(dandelion1);
        dandelionRepository.save(dandelion2);
        dandelionRepository.save(dandelion3);
        em.flush();
        em.clear();

        // when
        DandelionDetailSvcDto dandelionDetailSvcDto = dandelionService.getDandelionSeedDto(savedMember1.getSeq());

        // then
        assertThat(dandelionDetailSvcDto).isNull();
    }

    @Test
    @DisplayName("랜덤 민들레씨 정보 조회 - 상태가 FLYING 인 민들레가 없는 경우")
    void findRandomDandelionSeedWhenAllDandelionNotFlying() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        dandelion1.changeStatus(Dandelion.Status.HOLD);
        dandelion2.changeStatus(Dandelion.Status.HOLD);
        dandelion3.changeStatus(Dandelion.Status.HOLD);
        dandelionRepository.save(dandelion1);
        dandelionRepository.save(dandelion2);
        dandelionRepository.save(dandelion3);
        em.flush();
        em.clear();

        // when
        DandelionDetailSvcDto dandelionDetailSvcDto = dandelionService.getDandelionSeedDto(savedMember1.getSeq());

        // then
        assertThat(dandelionDetailSvcDto).isNull();
    }

    @Test
    @DisplayName("랜덤 민들레씨 정보 조회 - 모든 민들레의 소유자가 요청한 사람인 경우")
    void findRandomDandelionSeedWhenAllDandelionOwner() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        dandelionRepository.save(dandelion1);
        dandelionRepository.save(Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(member1.getAppConfig().getCommunity())
                .flowerSignNumber(2)
                .member(member1)
                .build());
        dandelionRepository.save(Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(member1.getAppConfig().getCommunity())
                .flowerSignNumber(3)
                .member(member1)
                .build());
        em.flush();
        em.clear();

        // when
        DandelionDetailSvcDto dandelionDetailSvcDto = dandelionService.getDandelionSeedDto(savedMember1.getSeq());

        // then
        assertThat(dandelionDetailSvcDto).isNull();
    }

    @Test
    @DisplayName("랜덤 민들레씨 정보 조회 - 모든 민들레를 잡아본 경우")
    void findRandomDandelionSeedWhenAllDandelionCatchBefore() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        dandelionRepository.save(dandelion1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        dandelionRepository.save(dandelion1);
        dandelionRepository.save(dandelion2);
        dandelionRepository.save(dandelion3);
        em.persist(MemberDandelionHistory.builder()
                .member(member1)
                .dandelion(dandelion2)
                .build());
        em.persist(MemberDandelionHistory.builder()
                .member(member1)
                .dandelion(dandelion3)
                .build());
        em.flush();
        em.clear();

        // when
        DandelionDetailSvcDto dandelionDetailSvcDto = dandelionService.getDandelionSeedDto(savedMember1.getSeq());

        // then
        assertThat(dandelionDetailSvcDto).isNull();
    }

    @Test
    @DisplayName("민들레 상태(Album) 확인 - True")
    void checkAlbumTrue() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.ALBUM);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isAlbum = dandelionService.isAlbum(savedDandelion.getSeq());

        // then
        assertThat(isAlbum).isTrue();
    }

    @Test
    @DisplayName("민들레 상태(Album) 확인 - False")
    void checkAlbumFalse() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.FLYING);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isAlbum = dandelionService.isAlbum(savedDandelion.getSeq());

        // then
        assertThat(isAlbum).isFalse();
    }

    @Test
    @DisplayName("민들레 상태(Album) 확인 - 예외 발생")
    void checkAlbumException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isAlbum(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 참여여부 확인 - 성공")
    void checkDandelionParticipatedTrue() {
        // given
        memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        petalRepository.save(Petal.builder()
                .message("와우 멋있어요")
                .imageFilename("/test/img")
                .nation("KOREA")
                .member(savedMember2)
                .dandelion(savedDandelion1)
                .build());

        em.flush();
        em.clear();

        // when
        boolean isParticipated = dandelionService.isParticipated(savedDandelion1.getSeq(), savedMember2.getSeq());

        // then
        assertThat(isParticipated).isTrue();
    }

    @Test
    @DisplayName("민들레 참여여부 확인 - 실패 (회원 존재X)")
    void checkDandelionParticipatedFalseNotExistMember() {
        // given
        memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        petalRepository.save(Petal.builder()
                .message("와우 멋있어요")
                .imageFilename("/test/img")
                .member(savedMember2)
                .nation("KOREA")
                .dandelion(savedDandelion1)
                .build());

        em.flush();
        em.clear();

        // when
        boolean isParticipated = dandelionService.isParticipated(savedDandelion1.getSeq(), 0L);

        // then
        assertThat(isParticipated).isFalse();
    }

    @Test
    @DisplayName("민들레 참여여부 확인 - 실패 (회원이 deleted)")
    void checkDandelionParticipatedFalseDeletedMember() {
        // given
        memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        petalRepository.save(Petal.builder()
                .message("와우 멋있어요")
                .imageFilename("/test/img")
                .member(savedMember2)
                .nation("KOREA")
                .dandelion(savedDandelion1)
                .build());

        savedMember2.delete();
        savedMember2.getPetals().forEach(petal -> petal.delete());

        em.flush();
        em.clear();

        // when
        boolean isParticipated = dandelionService.isParticipated(savedDandelion1.getSeq(), savedMember2.getSeq());

        // then
        assertThat(isParticipated).isFalse();
    }

    @Test
    @DisplayName("민들레 참여여부 확인 - 실패(민들레 존재 X)")
    void checkDandelionParticipatedFalseNotExistDandelion() {
        // given
        memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        petalRepository.save(Petal.builder()
                .message("와우 멋있어요")
                .imageFilename("/test/img")
                .member(savedMember2)
                .nation("KOREA")
                .dandelion(savedDandelion1)
                .build());

        em.flush();
        em.clear();

        // when
        boolean isParticipated = dandelionService.isParticipated(0L, savedMember2.getSeq());

        // then
        assertThat(isParticipated).isFalse();
    }

    @Test
    @DisplayName("민들레 참여여부 확인 - 실패 (민들레가 deleted")
    void checkDandelionParticipatedFalseDeletedDandelion() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Member savedMember3 = memberRepository.save(member3);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        petalRepository.save(Petal.builder()
                .message("와우 멋있어요")
                .imageFilename("/test/img.jpg")
                .member(savedMember2)
                .nation("KOREA")
                .dandelion(savedDandelion1)
                .build());

        petalRepository.save(Petal.builder()
                .message("호우 샷")
                .imageFilename("/test/img1.jpg")
                .member(savedMember3)
                .nation("CANADA")
                .dandelion(savedDandelion1)
                .build());

        em.flush();
        em.clear();

        // when
        dandelionService.deleteDandelion(savedDandelion1.getSeq(), savedMember1.getSeq());
        boolean isParticipated2 = dandelionService.isParticipated(savedDandelion1.getSeq(), savedMember2.getSeq());
        boolean isParticipated3 = dandelionService.isParticipated(savedDandelion1.getSeq(), savedMember3.getSeq());

        // then
        assertThat(isParticipated2).isFalse();
        assertThat(isParticipated3).isFalse();
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
        AlbumListPageSvcDto albumListPageSvcDto = dandelionService.getAlbumInfo(member1.getSeq(), 1, 3);

        // then
        assertThat(albumListPageSvcDto.getTotalDandelionCount()).isEqualTo(10);
        assertThat(albumListPageSvcDto.getTotalPageNum()).isEqualTo(4);
        assertThat(albumListPageSvcDto.getNowPageNum()).isEqualTo(1);
        assertThat(albumListPageSvcDto.getDandelionInfos().size()).isEqualTo(3);
        assertThat(albumListPageSvcDto.getDandelionInfos().get(0).getDescription()).isEqualTo("9");
        assertThat(albumListPageSvcDto.getDandelionInfos().get(1).getDescription()).isEqualTo("8");
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

        AlbumListPageSvcDto albumListPageSvcDto = dandelionService.getAlbumInfo(member1.getSeq(), 4, 3);

        assertThat(albumListPageSvcDto.getTotalDandelionCount()).isEqualTo(10);
        assertThat(albumListPageSvcDto.getTotalPageNum()).isEqualTo(4);
        assertThat(albumListPageSvcDto.getNowPageNum()).isEqualTo(4);
        assertThat(albumListPageSvcDto.getDandelionInfos().size()).isEqualTo(1);
        assertThat(albumListPageSvcDto.getDandelionInfos().get(0).getDescription()).isEqualTo("9");
    }

    @Test
    @DisplayName("꽃밭 앨범 페이지 조회 - 데이터가 없는 경우")
    void getAlbumPageWhenNotExistData() {
        // given
        memberRepository.save(member1);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        AlbumListPageSvcDto albumListPageSvcDto = dandelionService.getAlbumInfo(member1.getSeq(), 1, 1);

        // then
        assertThat(albumListPageSvcDto).isNull();
    }

    @Test
    @DisplayName("꽃밭 앨범 페이지 조회 - 없는 회원일 경우")
    void getAlbumPageWhenNotExistMember() {
        // given
        memberRepository.save(member1);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        AlbumListPageSvcDto albumListPageSvcDto = dandelionService.getAlbumInfo(0L, 1, 1);

        // then
        assertThat(albumListPageSvcDto).isNull();
    }

    @Test
    @DisplayName("민들레씨 생성 후 날리기 - 성공 (파일 X, 메시지 O)")
    void createDandelionSuccessNotExistFileAndExistMessage() throws IOException {
        //given
        Member savedMember1 = memberRepository.save(member1);
        dandelionRepository.save(dandelion1);

        em.flush();
        em.clear();

        //when
        DandelionCreateSvcDto newDandelionCreateSvcDto = DandelionCreateSvcDto.builder()
                .message("안녕 나는 피나코야")
                .blossomedDate(LocalDate.parse("2022-06-30", DateTimeFormatter.ISO_DATE))
                .nation("KOREA")
                .imageFile(null)
                .build();

        dandelionService.createDandelion(savedMember1.getSeq(), newDandelionCreateSvcDto);

        Member findMember1 = memberRepository.findBySeq(savedMember1.getSeq()).orElse(null);

        List<Dandelion> findActiveDandelions = dandelionRepository.findActiveDandelionListByMemberSeq(findMember1.getSeq());

        Dandelion findDandelion1 = findActiveDandelions.get(findActiveDandelions.size() - 1);

        List<Petal> findPetals = findDandelion1.getPetals();

        Petal findPetal1 = findPetals.get(findPetals.size() - 1);

        //then
        assertThat(findActiveDandelions.size()).isEqualTo(2);
        assertThat(findPetals.size()).isEqualTo(1);
        assertThat(findPetal1.getMessage()).isEqualTo("안녕 나는 피나코야");
        assertThat(findPetal1.getImageFilename()).isNull();
    }

    @Test
    @DisplayName("민들레씨 생성 후 날리기 - 실패 (유저존재 X)")
    void createDandelionFailNotExistMember() {
        //given
        memberRepository.save(member1);
        dandelionRepository.save(dandelion1);

        em.flush();
        em.clear();

        //when
        DandelionCreateSvcDto newDandelionCreateSvcDto = DandelionCreateSvcDto.builder()
                .message("안녕 나는 피나코야")
                .blossomedDate(LocalDate.parse("2022-06-30"))
                .imageFile(null)
                .build();

        //then
        assertThatThrownBy(() -> dandelionService.createDandelion(0L, newDandelionCreateSvcDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레씨 생성 후 날리기 - 실패 (유저가 deleted)")
    void createDandelionFailDeletedMember() {
        //given
        Member savedMember1 = memberRepository.save(member1);
        dandelionRepository.save(dandelion1);

        savedMember1.delete();

        em.flush();
        em.clear();

        //when
        DandelionCreateSvcDto newDandelionCreateSvcDto = DandelionCreateSvcDto.builder()
                .message("안녕 나는 피나코야")
                .blossomedDate(LocalDate.parse("2022-06-30"))
                .imageFile(null)
                .build();

        //then
        assertThatThrownBy(() -> dandelionService.createDandelion(savedMember1.getSeq(), newDandelionCreateSvcDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레씨 생성 후 날리기 - 실패 (해당 회원의 민들레씨가 5개인 경우)")
    void createDandelionFailMemberUseAllSeed() {
        //given
        Member savedMember1 = memberRepository.save(member1);
        dandelionRepository.save(dandelion1);

        for (int i = 2; i <= 5; i++) {
            dandelionRepository.save(Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-05-03"))
                    .community(Community.WORLD)
                    .flowerSignNumber(i)
                    .member(savedMember1)
                    .build());
        }

        em.flush();
        em.clear();

        //when
        DandelionCreateSvcDto newDandelionCreateSvcDto = DandelionCreateSvcDto.builder()
                .message("안녕 나는 피나코야")
                .blossomedDate(LocalDate.parse("2022-06-30"))
                .imageFile(null)
                .build();

        //then
        assertThatThrownBy(() -> dandelionService.createDandelion(savedMember1.getSeq(), newDandelionCreateSvcDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }
    @Test
    @DisplayName("민들레 상태(Hold) 확인 - True")
    void checkHoldTrue() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(savedMember1.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(savedMember1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.HOLD);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isHold = dandelionService.isHold(savedDandelion.getSeq());

        // then
        assertThat(isHold).isTrue();
    }

    @Test
    @DisplayName("민들레 상태(Hold) 확인 - False")
    void checkHoldFalse() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(savedMember1.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(savedMember1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.FLYING);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isHold = dandelionService.isHold(savedDandelion.getSeq());

        // then
        assertThat(isHold).isFalse();
    }

    @Test
    @DisplayName("민들레 상태(Hold) 확인 - 예외 발생")
    void checkHoldException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isHold(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레씨 최근 참여 여부 확인 - 민들레 데이터가 없는 경우")
    void checkRecentParticipantWhenNotExistDandelion() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isMostRecentParticipant(0L, savedMember1.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레씨 최근 참여 여부 확인 - 민들레가 삭제 처리된 경우")
    void checkRecentParticipantWhenDandelionDeleted() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        dandelion1.delete();
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isMostRecentParticipant(savedDandelion1.getSeq(), savedMember1.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레씨 최근 참여 여부 확인 - 회원이 삭제 처리된 경우")
    void checkRecentParticipantWhenMemberDeleted() {
        // given
        member1.delete();
        Member savedMember1 = memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isMostRecentParticipant(savedDandelion1.getSeq(), savedMember1.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레씨 최근 참여 여부 확인 - 가장 최근에 참여한 경우")
    void checkRecentParticipantTrue() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        dandelionRepository.save(dandelion1);
        memberRepository.save(member2);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        /**
         * 민들레씨 잡기 -> HOLD 처리됨
         */
        dandelionService.getDandelionSeedDto(savedMember1.getSeq());

        // when
        boolean isMostRecentParticipant = dandelionService.isMostRecentParticipant(savedDandelion2.getSeq(), savedMember1.getSeq());

        // then
        assertThat(isMostRecentParticipant).isTrue();
    }

    @Test
    @DisplayName("민들레씨 최근 참여 여부 확인 - 가장 최근에 참여한 민들레가 아닌 경우")
    void checkRecentParticipantFalse() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        dandelionRepository.save(dandelion1);

        memberRepository.save(member2);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);

        memberRepository.save(member3);
        dandelionRepository.save(dandelion3);
        em.flush();
        em.clear();

        /**
         * 민들레씨 잡기 -> HOLD 처리됨
         * dandelion2 -> dandelion3 순으로 잡음
         */
        dandelionService.getDandelionSeedDto(savedMember1.getSeq());
        dandelionService.getDandelionSeedDto(savedMember1.getSeq());

        // when
        boolean isMostRecentParticipant = dandelionService.isMostRecentParticipant(savedDandelion2.getSeq(), savedMember1.getSeq());

        // then
        assertThat(isMostRecentParticipant).isFalse();
    }

    @Test
    @DisplayName("민들레 상태(Flying) 확인 - true")
    void checkFlyingTrue() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(savedMember1.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(savedMember1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.FLYING);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isFlying = dandelionService.isFlying(savedDandelion.getSeq());

        // then
        assertThat(isFlying).isTrue();
    }

    @Test
    @DisplayName("민들레 상태(Flying) 확인 - False")
    void checkFlyingFalse() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(savedMember1.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(savedMember1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.HOLD);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isFlying = dandelionService.isFlying(savedDandelion.getSeq());

        // then
        assertThat(isFlying).isFalse();
    }

    @Test
    @DisplayName("민들레 상태(Flying) 확인 - 데이터 없어서 예외 발생")
    void checkFlyingExceptionWhenNotExists() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isFlying(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 상태(Flying) 확인 - 삭체 처리되어 예외 발생")
    void checkFlyingExceptionWhenDeleted() {
        // given
        memberRepository.save(member1);
        dandelion1.delete();
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isFlying(savedDandelion1.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("꽃잎 추가 - 예외 발생 : 회원 데이터 없음")
    void addPetalExceptionWhenNotExistMember() throws IOException {
        // given
        memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        PetalCreateSvcDto newPetalCreateSvcDto = PetalCreateSvcDto.builder()
                .message("하하하하하하하")
                .imageFile(null)
                .nation("KOREA")
                .build();

        // when

        // then
        assertThatThrownBy(() -> dandelionService.addPetal(0L, savedDandelion1.getSeq(), newPetalCreateSvcDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("꽃잎 추가 - 예외 발생 : 회원 탈퇴")
    void addPetalExceptionWhenDeletedMember() {
        // given
        member1.delete();
        Member savedMember1 = memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        PetalCreateSvcDto newPetalCreateSvcDto = PetalCreateSvcDto.builder()
                .message("하하하하하하하")
                .imageFile(null)
                .nation("KOREA")
                .build();

        // when

        // then
        assertThatThrownBy(() -> dandelionService.addPetal(savedMember1.getSeq(), savedDandelion1.getSeq(), newPetalCreateSvcDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("꽃잎 추가 - 예외 발생 : 민들레 데이터 없음")
    void addPetalExceptionWhenNotExistDandelion() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        dandelionRepository.save(dandelion1);
        PetalCreateSvcDto newPetalCreateSvcDto = PetalCreateSvcDto.builder()
                .message("하하하하하하하")
                .imageFile(null)
                .nation("KOREA")
                .build();

        // when

        // then
        assertThatThrownBy(() -> dandelionService.addPetal(savedMember1.getSeq(), 0L, newPetalCreateSvcDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("꽃잎 추가 - 예외 발생 : 민들레 삭제 처리")
    void addPetalExceptionWhenDeletedDandelion() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        dandelion1.delete();
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        PetalCreateSvcDto newPetalCreateSvcDto = PetalCreateSvcDto.builder()
                .message("하하하하하하하")
                .imageFile(null)
                .nation("KOREA")
                .build();

        // when

        // then
        assertThatThrownBy(() -> dandelionService.addPetal(savedMember1.getSeq(), savedDandelion1.getSeq(), newPetalCreateSvcDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("꽃잎 추가 - 성공")
    void addPetalSuccess() throws IOException {
        // given
        Member newMember = Member.builder()
                .id("newId")
                .password("newPassword")
                .build();
        AppConfig.builder()
                .member(newMember)
                .language(AppConfig.Language.KOREAN)
                .build();
        Member newSavedMember = memberRepository.save(newMember);
        em.flush();
        em.clear();

        DandelionCreateSvcDto newDandelion = DandelionCreateSvcDto.builder()
                .message("하하하하하")
                .imageFile(null)
                .blossomedDate(LocalDate.parse("2022-05-30"))
                .nation("KOREA")
                .build();
        Dandelion savedDandelion = dandelionService.createDandelion(newSavedMember.getSeq(), newDandelion);

        Member savedMember1 = memberRepository.save(member1);
        PetalCreateSvcDto newPetalCreateSvcDto = PetalCreateSvcDto.builder()
                .message("하하하하하하하")
                .imageFile(null)
                .nation("KOREA")
                .build();

        // when
        Petal savedPetal = dandelionService.addPetal(savedMember1.getSeq(), savedDandelion.getSeq(), newPetalCreateSvcDto);
        em.flush();
        em.clear();
        Petal findPetal = petalRepository.findBySeq(savedPetal.getSeq())
                .orElse(null);
        Dandelion findDandelion1 = dandelionRepository.findBySeq(savedDandelion.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion1.getPetals().size()).isEqualTo(2);
        assertThat(findDandelion1.getStatus()).isEqualTo(Dandelion.Status.FLYING);
        assertThat(findPetal.getMessage()).isEqualTo(newPetalCreateSvcDto.getMessage());
        assertThat(findPetal.getImageFilename()).isNull();
        assertThat(findPetal.getNation()).isEqualTo("KOREA");
    }

    @Test
    @DisplayName("꽃잎 추가 - 성공(READY 상태 유지)")
    void addPetalSuccessReady() throws IOException {
        // given
        Member newMember = Member.builder()
                .id("newId")
                .password("newPassword")
                .build();
        AppConfig.builder()
                .member(newMember)
                .language(AppConfig.Language.KOREAN)
                .build();
        Member newSavedMember = memberRepository.save(newMember);
        em.flush();
        em.clear();

        DandelionCreateSvcDto newDandelion = DandelionCreateSvcDto.builder()
                .message("하하하하하")
                .imageFile(null)
                .blossomedDate(LocalDate.parse("2022-05-30"))
                .nation("KOREA")
                .build();
        Dandelion savedDandelion = dandelionService.createDandelion(newSavedMember.getSeq(), newDandelion);

        Member savedMember1 = memberRepository.save(member1);
        PetalCreateSvcDto newPetalCreateSvcDto = PetalCreateSvcDto.builder()
                .message("하하하하하하하")
                .imageFile(null)
                .nation("KOREA")
                .build();
        Dandelion flyingDandelion = dandelionRepository.findBySeq(savedDandelion.getSeq())
                .orElse(null);
        flyingDandelion.changeStatus(Dandelion.Status.READY);
        em.flush();
        em.clear();

        // when
        Petal savedPetal = dandelionService.addPetal(savedMember1.getSeq(), savedDandelion.getSeq(), newPetalCreateSvcDto);
        em.flush();
        em.clear();
        Petal findPetal = petalRepository.findBySeq(savedPetal.getSeq())
                .orElse(null);
        Dandelion findDandelion1 = dandelionRepository.findBySeq(savedDandelion.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion1.getPetals().size()).isEqualTo(2);
        assertThat(findDandelion1.getStatus()).isEqualTo(Dandelion.Status.READY);
        assertThat(findPetal.getMessage()).isEqualTo(newPetalCreateSvcDto.getMessage());
        assertThat(findPetal.getImageFilename()).isNull();
        assertThat(findPetal.getNation()).isEqualTo("KOREA");
    }

    @Test
    @DisplayName("꽃잎 추가 - 예외 발생 : 중복 추가")
    void addPetalExceptionWhenMultiplePetal() throws IOException {
        // given
        Member savedMember1 = memberRepository.save(member1);
        DandelionCreateSvcDto newDandelion = DandelionCreateSvcDto.builder()
                .message("하하하하하")
                .imageFile(null)
                .blossomedDate(LocalDate.parse("2022-05-30"))
                .nation("KOREA")
                .build();
        Dandelion savedDandelion = dandelionService.createDandelion(savedMember1.getSeq(), newDandelion);

        PetalCreateSvcDto newPetalCreateSvcDto = PetalCreateSvcDto.builder()
                .message("하하하하하하하")
                .imageFile(null)
                .nation("KOREA")
                .build();

        // when

        // then
        assertThatThrownBy(() -> dandelionService.addPetal(savedMember1.getSeq(), savedDandelion.getSeq(), newPetalCreateSvcDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }


    @Test
    @DisplayName("민들레 상세조회 기능 - 성공(blossomed)")
    void getDandelionDetailSuccessBlossomed() {
        //given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);

        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Petal savedPetal1 = petalRepository.save(Petal.builder()
                .message("안녕하세요!")
                .dandelion(savedDandelion1)
                .member(savedMember1)
                .nation("KOREA")
                .imageFilename("testImg.jpg").build());

        em.flush();
        em.clear();

        //when
        Dandelion findDandelion1 = dandelionRepository.findBySeq(savedDandelion1.getSeq()).orElse(null);
        Member findMember2 = memberRepository.findBySeq(savedMember2.getSeq()).orElse(null);

        Petal savedPetal2 = petalRepository.save(Petal.builder()
                .message("오 안녕안녕")
                .dandelion(findDandelion1)
                .member(findMember2)
                .nation("KOREA")
                .imageFilename("pinako.jpg").build());

        findDandelion1.changeStatus(Dandelion.Status.BLOSSOMED);

        em.flush();
        em.clear();

        DandelionDetailSvcDto dandelionDetail = dandelionService.getDandelionDetail(findDandelion1.getSeq(), savedMember1.getSeq());
        List<DandelionDetailSvcDto.PetalInfo> petalInfos = dandelionDetail.getPetalInfos();

        //then
        assertThat(petalInfos.size()).isEqualTo(2);
        assertThat(petalInfos.get(0).getSeq()).isEqualTo(savedPetal1.getSeq());
        assertThat(petalInfos.get(1).getSeq()).isEqualTo(savedPetal2.getSeq());
        assertThat(petalInfos.get(0).getMessage()).isEqualTo("안녕하세요!");
        assertThat(petalInfos.get(1).getMessage()).isEqualTo("오 안녕안녕");
    }

    @Test
    @DisplayName("민들레 상세조회 기능 - 성공(album)")
    void getDandelionDetailSuccessAlbum() {
        //given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);

        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Petal savedPetal1 = petalRepository.save(Petal.builder()
                .message("안녕하세요!")
                .dandelion(savedDandelion1)
                .member(savedMember1)
                .nation("KOREA")
                .imageFilename("testImg.jpg").build());

        em.flush();
        em.clear();

        //when
        Dandelion findDandelion1 = dandelionRepository.findBySeq(savedDandelion1.getSeq()).orElse(null);
        Member findMember2 = memberRepository.findBySeq(savedMember2.getSeq()).orElse(null);

        Petal savedPetal2 = petalRepository.save(Petal.builder()
                .message("오 안녕안녕")
                .dandelion(findDandelion1)
                .member(findMember2)
                .nation("KOREA")
                .imageFilename("pinako.jpg").build());

        findDandelion1.changeStatus(Dandelion.Status.ALBUM);

        em.flush();
        em.clear();

        DandelionDetailSvcDto dandelionDetail = dandelionService.getDandelionDetail(findDandelion1.getSeq(), savedMember1.getSeq());
        List<DandelionDetailSvcDto.PetalInfo> petalInfos = dandelionDetail.getPetalInfos();

        //then
        assertThat(petalInfos.size()).isEqualTo(2);
        assertThat(petalInfos.get(0).getSeq()).isEqualTo(savedPetal1.getSeq());
        assertThat(petalInfos.get(1).getSeq()).isEqualTo(savedPetal2.getSeq());
        assertThat(petalInfos.get(0).getMessage()).isEqualTo("안녕하세요!");
        assertThat(petalInfos.get(1).getMessage()).isEqualTo("오 안녕안녕");
    }

    @Test
    @DisplayName("민들레 상세조회 기능 - 성공(participated)")
    void getDandelionDetailSuccessParticipated() {
        //given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);

        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Petal savedPetal1 = petalRepository.save(Petal.builder()
                .message("안녕하세요!")
                .dandelion(savedDandelion1)
                .member(savedMember1)
                .nation("KOREA")
                .imageFilename("testImg.jpg").build());

        em.flush();
        em.clear();

        //when
        Dandelion findDandelion1 = dandelionRepository.findBySeq(savedDandelion1.getSeq()).orElse(null);
        Member findMember2 = memberRepository.findBySeq(savedMember2.getSeq()).orElse(null);

        Petal savedPetal2 = petalRepository.save(Petal.builder()
                .message("오 안녕안녕")
                .dandelion(findDandelion1)
                .member(findMember2)
                .nation("KOREA")
                .imageFilename("pinako.jpg").build());

        findDandelion1.changeStatus(Dandelion.Status.ALBUM);

        em.flush();
        em.clear();

        DandelionDetailSvcDto dandelionDetail = dandelionService.getDandelionDetail(findDandelion1.getSeq(), findMember2.getSeq());
        List<DandelionDetailSvcDto.PetalInfo> petalInfos = dandelionDetail.getPetalInfos();

        //then
        assertThat(petalInfos.size()).isEqualTo(2);
        assertThat(petalInfos.get(0).getSeq()).isEqualTo(savedPetal1.getSeq());
        assertThat(petalInfos.get(1).getSeq()).isEqualTo(savedPetal2.getSeq());
        assertThat(petalInfos.get(0).getMessage()).isEqualTo("안녕하세요!");
        assertThat(petalInfos.get(1).getMessage()).isEqualTo("오 안녕안녕");
    }

    @Test
    @DisplayName("민들레 상세조회 기능 - 실패(회원 존재X)")
    void getDandelionDetailFailNotExistMember() {
        //given
        Member savedMember1 = memberRepository.save(member1);

        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        petalRepository.save(Petal.builder()
                .message("안녕하세요!")
                .dandelion(savedDandelion1)
                .member(savedMember1)
                .nation("KOREA")
                .imageFilename("testImg.jpg").build());

        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> dandelionService.getDandelionDetail(savedDandelion1.getSeq(), 0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());

    }

    @Test
    @DisplayName("민들레 상세조회 기능 - 실패(회원 deleted)")
    void getDandelionDetailFailDeletedMember() {
        //given
        Member savedMember1 = memberRepository.save(member1);

        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        petalRepository.save(Petal.builder()
                .message("안녕하세요!")
                .dandelion(savedDandelion1)
                .member(savedMember1)
                .nation("KOREA")
                .imageFilename("testImg.jpg").build());

        savedMember1.delete();

        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> dandelionService.getDandelionDetail(savedDandelion1.getSeq(), savedMember1.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());

    }

    @Test
    @DisplayName("민들레 상세조회 기능 - 실패(민들레 존재X)")
    void getDandelionDetailFailNotExistDandelion() {
        //given
        Member savedMember1 = memberRepository.save(member1);

        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        petalRepository.save(Petal.builder()
                .message("안녕하세요!")
                .dandelion(savedDandelion1)
                .member(savedMember1)
                .nation("KOREA")
                .imageFilename("testImg.jpg").build());

        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> dandelionService.getDandelionDetail(0L, savedMember1.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());

    }

    @Test
    @DisplayName("민들레 상세조회 기능 - 실패(민들레 deleted)")
    void getDandelionDetailFailDeletedDandelion() {
        //given
        Member savedMember1 = memberRepository.save(member1);

        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        petalRepository.save(Petal.builder()
                .message("안녕하세요!")
                .dandelion(savedDandelion1)
                .member(savedMember1)
                .nation("KOREA")
                .imageFilename("testImg.jpg").build());

        savedDandelion1.delete();

        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> dandelionService.getDandelionDetail(savedDandelion1.getSeq(), savedMember1.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());

    }

    @Test
    @DisplayName("기록 보관함 조회 페이징 - 데이터가 있는 경우")
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

        for (int i = 0; i < 21; i++) {
            Tag tag = Tag.builder()
                    .name("TagNum = " + i)
                    .dandelion(dandelions.get(i))
                    .member(member2)
                    .build();
            tagRepository.save(tag);
            em.flush();
        }
        em.clear();

        // when then

        //첫번째 페이지
        int page = 1;
        int size = 3;
        ParticipationListPageSvcDto participationListPageSvcDto = dandelionService.getParticipationInfo(member2.getSeq(), PageRequest.of(page, size));

        assertThat(participationListPageSvcDto.getTotalDandelionCount()).isEqualTo(14);
        assertThat(participationListPageSvcDto.getTotalPageNum()).isEqualTo(5);
        assertThat(participationListPageSvcDto.getNowPageNum()).isEqualTo(1);
        assertThat(participationListPageSvcDto.getDandelionInfos().size()).isEqualTo(3);
        assertThat(participationListPageSvcDto.getDandelionInfos().get(0).getTagInfos().size()).isEqualTo(1);
        assertThat(participationListPageSvcDto.getDandelionInfos().get(0).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 0");
        assertThat(participationListPageSvcDto.getDandelionInfos().get(1).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 1");
        assertThat(participationListPageSvcDto.getDandelionInfos().get(2).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 3");

        //마지막 페이지
        page = 5;
        size = 3;
        participationListPageSvcDto = dandelionService.getParticipationInfo(member2.getSeq(), PageRequest.of(page, size));

        assertThat(participationListPageSvcDto.getTotalDandelionCount()).isEqualTo(14);
        assertThat(participationListPageSvcDto.getTotalPageNum()).isEqualTo(5);
        assertThat(participationListPageSvcDto.getNowPageNum()).isEqualTo(5);
        assertThat(participationListPageSvcDto.getDandelionInfos().size()).isEqualTo(2);
        assertThat(participationListPageSvcDto.getDandelionInfos().get(0).getTagInfos().size()).isEqualTo(1);
        assertThat(participationListPageSvcDto.getDandelionInfos().get(0).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 18");
        assertThat(participationListPageSvcDto.getDandelionInfos().get(1).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 19");

        // 0번 민들레 삭제
        dandelions.get(0).delete();
        dandelionRepository.save(dandelions.get(0));
        em.flush();
        em.clear();

        page = 1;
        size = 3;
        participationListPageSvcDto = dandelionService.getParticipationInfo(member2.getSeq(), PageRequest.of(page, size));

        assertThat(participationListPageSvcDto.getDandelionInfos().get(0).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 1");
        assertThat(participationListPageSvcDto.getDandelionInfos().get(1).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 3");
        assertThat(participationListPageSvcDto.getDandelionInfos().get(2).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 4");

        // 3번 민들레 꽃잎 삭제
        dandelions.get(3).getPetals().get(0).delete();
        petalRepository.save(dandelions.get(3).getPetals().get(0));
        dandelionRepository.save(dandelions.get(3));
        em.flush();
        em.clear();

        participationListPageSvcDto = dandelionService.getParticipationInfo(member2.getSeq(), PageRequest.of(page, size));

        assertThat(participationListPageSvcDto.getDandelionInfos().get(0).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 1");
        assertThat(participationListPageSvcDto.getDandelionInfos().get(1).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 4");
        assertThat(participationListPageSvcDto.getDandelionInfos().get(2).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 6");

        // 4번 민들레 태그 삭제
        tagRepository.delete(dandelions.get(4).getTags().get(0));
        em.flush();
        em.clear();


        participationListPageSvcDto = dandelionService.getParticipationInfo(member2.getSeq(), PageRequest.of(page, size));

        assertThat(participationListPageSvcDto.getDandelionInfos().get(0).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 1");
        assertThat(participationListPageSvcDto.getDandelionInfos().get(1).getTagInfos().size()).isEqualTo(0);
        assertThat(participationListPageSvcDto.getDandelionInfos().get(2).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 6");

        // 민들레 주인 탈퇴

        Member findMember1 = memberRepository.findBySeq(member1.getSeq()).orElse(null);
        findMember1.delete();

        em.flush();
        em.clear();

        participationListPageSvcDto = dandelionService.getParticipationInfo(member2.getSeq(), PageRequest.of(page, size));
        assertThat(participationListPageSvcDto).isNull();
    }

    @Test
    @DisplayName("기록 보관함 조회 페이징 - 데이터가 없는 경우")
    void getParticipationPageNoneData() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        em.flush();
        em.flush();

        // when
        int page = 1;
        int size = 3;
        ParticipationListPageSvcDto participationListPageSvcDto = dandelionService.getParticipationInfo(member2.getSeq(), PageRequest.of(page, size));

        // then
        assertThat(participationListPageSvcDto).isNull();
    }

    public String getContentImagePath(String imageFilename){
        return imageFilename == null ? null : fileStorageUrl + contentImagePath + imageFilename;
    }

    @Test
    @DisplayName("민들레 상태(Ready) 확인 - True")
    void checkReadyTrue() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.READY);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isReady = dandelionService.isReady(savedDandelion.getSeq());

        // then
        assertThat(isReady).isTrue();
    }

    @Test
    @DisplayName("민들레 상태(Ready) 확인 - False")
    void checkReadyFalse() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.FLYING);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isReady = dandelionService.isReady(savedDandelion.getSeq());

        // then
        assertThat(isReady).isFalse();
    }

    @Test
    @DisplayName("민들레 상태(Ready) 확인 - 예외 발생")
    void checkReadyException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isReady(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }
}