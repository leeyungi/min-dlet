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
                .id("?????????1")
                .password("????????????1")
                .build();

        member2 = Member.builder()
                .id("?????????2")
                .password("????????????2")
                .build();

        member3 = Member.builder()
                .id("?????????3")
                .password("????????????3")
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
    @DisplayName("????????? ??????(Blossomed) ?????? - True")
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
    @DisplayName("????????? ??????(Blossomed) ?????? - False")
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
    @DisplayName("????????? ??????(Blossomed) ?????? - ?????? ??????")
    void checkBlossomedException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isBlossomed(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("????????? ?????? ?????? - ????????? ?????? ??????")
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
    @DisplayName("????????? ?????? ?????? - ????????? ?????? ??????")
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
    @DisplayName("????????? ?????? ?????? - ?????? ??????")
    void checkOwnerException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isOwner(0L, 0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("????????? ?????? ?????? - ??????")
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
    @DisplayName("????????? ?????? ?????? - ?????? ??????")
    void changeDescriptionException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.changeDescription(0L, "??????"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? - ?????? ??????: ?????? ????????? ???????????? ?????? ??????")
    void countLeftSeedWhenNotExistMember() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.getLeftSeedCount(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? - ???????????? 1?????? ???????????? ?????? ??????")
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
    @DisplayName("?????? ?????? ?????? ?????? - flying ????????? ???????????? 1??? ????????? ??????")
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
    @DisplayName("?????? ?????? ?????? ?????? - flying ????????? ???????????? 1??? ????????? ????????? ??????")
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
    @DisplayName("?????? ?????? ?????? ?????? - hold ????????? ???????????? 1??? ????????? ??????")
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
    @DisplayName("?????? ?????? ?????? ?????? - hold ????????? ???????????? 1??? ????????? ????????? ??????")
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
    @DisplayName("?????? ?????? ?????? ?????? - return ????????? ???????????? 1??? ????????? ??????")
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
    @DisplayName("?????? ?????? ?????? ?????? - return ????????? ???????????? 1??? ????????? ????????? ??????")
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
    @DisplayName("?????? ?????? ?????? ?????? - pending ????????? ???????????? 1??? ????????? ??????")
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
    @DisplayName("?????? ?????? ?????? ?????? - pending ????????? ???????????? 1??? ????????? ????????? ??????")
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
    @DisplayName("?????? ?????? ?????? ?????? - blossomed ????????? ???????????? 1??? ????????? ??????")
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
    @DisplayName("?????? ?????? ?????? ?????? - blossomed ????????? ???????????? 1??? ????????? ????????? ??????")
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
    @DisplayName("?????? ?????? ?????? ?????? - blocked ????????? ???????????? 1?????? ??????")
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
    @DisplayName("?????? ?????? ?????? ?????? - album ????????? ???????????? 1?????? ??????")
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
    @DisplayName("?????? ?????? ?????? ?????? - ??? ????????? 6?????? ???????????? ????????? ??????")
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
    @DisplayName("????????? ??????(Return) ?????? - True")
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
    @DisplayName("????????? ??????(Return) ?????? - False")
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
    @DisplayName("????????? ??????(Return) ?????? - ?????? ??????")
    void checkReturnException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isReturn(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("????????? ?????? ??????")
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
    @DisplayName("????????? ?????? ?????? - ?????? ??????")
    void changeDandelionStatusException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.changeStatus(0L, Dandelion.Status.FLYING))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("????????? ?????? ?????? ??????")
    void deleteDandelionTagSuccess() {
        // given
        memberRepository.save(member1);
        Member savedMember3 = memberRepository.save(member3);
        Dandelion savedDandelion = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        tag1 = Tag.builder()
                .dandelion(savedDandelion)
                .name("2022??? ??? ??????")
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
    @DisplayName("????????? ?????? ?????? ?????? - Tag ??????")
    void deleteDandelionTagFailNonTag() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.deleteTag(0L, 1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????? - Seq ??? ??????")
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
                .name("2022??? ??? ??????")
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
    @DisplayName("????????? ?????? ??????")
    void deleteDandelionSuccess() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        Petal savedPetal1 = petalRepository.save(
                Petal.builder()
                        .member(savedMember1)
                        .dandelion(savedDandelion1)
                        .message("???????????????!!!")
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
    @DisplayName("????????? ?????? ?????? - ????????? ??????")
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
    @DisplayName("????????? ?????? ?????? - ?????? ???????????? ??? ??????")
    void deleteDandelionFailNotEqualMemberSeq() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        petalRepository.save(
                Petal.builder()
                        .member(savedMember1)
                        .dandelion(savedDandelion1)
                        .message("???????????????!!!")
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
    @DisplayName("????????? ?????? ?????? - ????????? deleted")
    void deleteDandelionFailMemberDeleted() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        savedMember1.delete();
        petalRepository.save(
                Petal.builder()
                        .member(savedMember1)
                        .dandelion(savedDandelion1)
                        .message("???????????????!!!")
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
    @DisplayName("?????? ?????? ?????? - ??????")
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
    @DisplayName("?????? ?????? ?????? - ?????? ?????? ??????")
    void getGardenInfoListNoneMember() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.getGardenInfoList(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ????????? ?????? ??????")
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
    @DisplayName("?????? ?????? ?????? - ????????? ????????? ??????")
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
    @DisplayName("?????? ?????? ?????? - blocked ????????? ????????? ??????")
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
    @DisplayName("?????? ?????? ?????? - album ????????? ????????? ??????")
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
    @DisplayName("?????? ???????????? ?????? ?????? - ??????")
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
                .message("?????????1")
                .imageFilename("?????????1")
                .nation("??????1")
                .dandelion(savedDandelion1)
                .member(savedMember1)
                .build());
        Petal savedPetal2 = petalRepository.save(Petal.builder()
                .message("?????????2")
                .imageFilename("?????????2")
                .nation("??????2")
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
         * DTO ????????? ????????? ??????
         */
        assertThat(dandelionDetailSvcDto.getDandelionSeq()).isEqualTo(savedDandelion1.getSeq());

        /**
         * ?????? ????????? ??????
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
         * ????????? ????????? ??????
         */
        assertThat(findDandelion.getStatus()).isEqualTo(Dandelion.Status.HOLD);
        assertThat(findDandelion.getCommunity()).isEqualTo(savedMember3.getAppConfig().getCommunity());
        assertThat(findDandelion.getMember().getSeq()).isNotEqualTo(savedMember3.getSeq());

        /**
         * ????????? ?????? ?????? ????????? ??????
         */
        assertThat(findMemberDandelionHistory.getMember().getSeq()).isEqualTo(savedMember3.getSeq());
        assertThat(findMemberDandelionHistory.getDandelion().getSeq()).isEqualTo(savedDandelion1.getSeq());
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ?????? - ????????? ???????????? ?????? ??????")
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
    @DisplayName("?????? ???????????? ?????? ?????? - ?????? ???????????? ????????? ??????")
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
    @DisplayName("?????? ???????????? ?????? ?????? - ????????? FLYING ??? ???????????? ?????? ??????")
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
    @DisplayName("?????? ???????????? ?????? ?????? - ?????? ???????????? ???????????? ????????? ????????? ??????")
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
    @DisplayName("?????? ???????????? ?????? ?????? - ?????? ???????????? ????????? ??????")
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
    @DisplayName("????????? ??????(Album) ?????? - True")
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
    @DisplayName("????????? ??????(Album) ?????? - False")
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
    @DisplayName("????????? ??????(Album) ?????? - ?????? ??????")
    void checkAlbumException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isAlbum(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("????????? ???????????? ?????? - ??????")
    void checkDandelionParticipatedTrue() {
        // given
        memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        petalRepository.save(Petal.builder()
                .message("?????? ????????????")
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
    @DisplayName("????????? ???????????? ?????? - ?????? (?????? ??????X)")
    void checkDandelionParticipatedFalseNotExistMember() {
        // given
        memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        petalRepository.save(Petal.builder()
                .message("?????? ????????????")
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
    @DisplayName("????????? ???????????? ?????? - ?????? (????????? deleted)")
    void checkDandelionParticipatedFalseDeletedMember() {
        // given
        memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        petalRepository.save(Petal.builder()
                .message("?????? ????????????")
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
    @DisplayName("????????? ???????????? ?????? - ??????(????????? ?????? X)")
    void checkDandelionParticipatedFalseNotExistDandelion() {
        // given
        memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        petalRepository.save(Petal.builder()
                .message("?????? ????????????")
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
    @DisplayName("????????? ???????????? ?????? - ?????? (???????????? deleted")
    void checkDandelionParticipatedFalseDeletedDandelion() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Member savedMember3 = memberRepository.save(member3);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        petalRepository.save(Petal.builder()
                .message("?????? ????????????")
                .imageFilename("/test/img.jpg")
                .member(savedMember2)
                .nation("KOREA")
                .dandelion(savedDandelion1)
                .build());

        petalRepository.save(Petal.builder()
                .message("?????? ???")
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
    @DisplayName("?????? ?????? ????????? ?????? - ???????????? ?????? ??????(????????? ?????????)")
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
    @DisplayName("?????? ?????? ????????? ?????? - ???????????? ?????? ??????(????????? ?????????)")
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
    @DisplayName("?????? ?????? ????????? ?????? - ???????????? ?????? ??????")
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
    @DisplayName("?????? ?????? ????????? ?????? - ?????? ????????? ??????")
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
    @DisplayName("???????????? ?????? ??? ????????? - ?????? (?????? X, ????????? O)")
    void createDandelionSuccessNotExistFileAndExistMessage() throws IOException {
        //given
        Member savedMember1 = memberRepository.save(member1);
        dandelionRepository.save(dandelion1);

        em.flush();
        em.clear();

        //when
        DandelionCreateSvcDto newDandelionCreateSvcDto = DandelionCreateSvcDto.builder()
                .message("?????? ?????? ????????????")
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
        assertThat(findPetal1.getMessage()).isEqualTo("?????? ?????? ????????????");
        assertThat(findPetal1.getImageFilename()).isNull();
    }

    @Test
    @DisplayName("???????????? ?????? ??? ????????? - ?????? (???????????? X)")
    void createDandelionFailNotExistMember() {
        //given
        memberRepository.save(member1);
        dandelionRepository.save(dandelion1);

        em.flush();
        em.clear();

        //when
        DandelionCreateSvcDto newDandelionCreateSvcDto = DandelionCreateSvcDto.builder()
                .message("?????? ?????? ????????????")
                .blossomedDate(LocalDate.parse("2022-06-30"))
                .imageFile(null)
                .build();

        //then
        assertThatThrownBy(() -> dandelionService.createDandelion(0L, newDandelionCreateSvcDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("???????????? ?????? ??? ????????? - ?????? (????????? deleted)")
    void createDandelionFailDeletedMember() {
        //given
        Member savedMember1 = memberRepository.save(member1);
        dandelionRepository.save(dandelion1);

        savedMember1.delete();

        em.flush();
        em.clear();

        //when
        DandelionCreateSvcDto newDandelionCreateSvcDto = DandelionCreateSvcDto.builder()
                .message("?????? ?????? ????????????")
                .blossomedDate(LocalDate.parse("2022-06-30"))
                .imageFile(null)
                .build();

        //then
        assertThatThrownBy(() -> dandelionService.createDandelion(savedMember1.getSeq(), newDandelionCreateSvcDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("???????????? ?????? ??? ????????? - ?????? (?????? ????????? ??????????????? 5?????? ??????)")
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
                .message("?????? ?????? ????????????")
                .blossomedDate(LocalDate.parse("2022-06-30"))
                .imageFile(null)
                .build();

        //then
        assertThatThrownBy(() -> dandelionService.createDandelion(savedMember1.getSeq(), newDandelionCreateSvcDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }
    @Test
    @DisplayName("????????? ??????(Hold) ?????? - True")
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
    @DisplayName("????????? ??????(Hold) ?????? - False")
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
    @DisplayName("????????? ??????(Hold) ?????? - ?????? ??????")
    void checkHoldException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isHold(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("???????????? ?????? ?????? ?????? ?????? - ????????? ???????????? ?????? ??????")
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
    @DisplayName("???????????? ?????? ?????? ?????? ?????? - ???????????? ?????? ????????? ??????")
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
    @DisplayName("???????????? ?????? ?????? ?????? ?????? - ????????? ?????? ????????? ??????")
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
    @DisplayName("???????????? ?????? ?????? ?????? ?????? - ?????? ????????? ????????? ??????")
    void checkRecentParticipantTrue() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        dandelionRepository.save(dandelion1);
        memberRepository.save(member2);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        /**
         * ???????????? ?????? -> HOLD ?????????
         */
        dandelionService.getDandelionSeedDto(savedMember1.getSeq());

        // when
        boolean isMostRecentParticipant = dandelionService.isMostRecentParticipant(savedDandelion2.getSeq(), savedMember1.getSeq());

        // then
        assertThat(isMostRecentParticipant).isTrue();
    }

    @Test
    @DisplayName("???????????? ?????? ?????? ?????? ?????? - ?????? ????????? ????????? ???????????? ?????? ??????")
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
         * ???????????? ?????? -> HOLD ?????????
         * dandelion2 -> dandelion3 ????????? ??????
         */
        dandelionService.getDandelionSeedDto(savedMember1.getSeq());
        dandelionService.getDandelionSeedDto(savedMember1.getSeq());

        // when
        boolean isMostRecentParticipant = dandelionService.isMostRecentParticipant(savedDandelion2.getSeq(), savedMember1.getSeq());

        // then
        assertThat(isMostRecentParticipant).isFalse();
    }

    @Test
    @DisplayName("????????? ??????(Flying) ?????? - true")
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
    @DisplayName("????????? ??????(Flying) ?????? - False")
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
    @DisplayName("????????? ??????(Flying) ?????? - ????????? ????????? ?????? ??????")
    void checkFlyingExceptionWhenNotExists() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isFlying(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("????????? ??????(Flying) ?????? - ?????? ???????????? ?????? ??????")
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
    @DisplayName("?????? ?????? - ?????? ?????? : ?????? ????????? ??????")
    void addPetalExceptionWhenNotExistMember() throws IOException {
        // given
        memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        PetalCreateSvcDto newPetalCreateSvcDto = PetalCreateSvcDto.builder()
                .message("?????????????????????")
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
    @DisplayName("?????? ?????? - ?????? ?????? : ?????? ??????")
    void addPetalExceptionWhenDeletedMember() {
        // given
        member1.delete();
        Member savedMember1 = memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        PetalCreateSvcDto newPetalCreateSvcDto = PetalCreateSvcDto.builder()
                .message("?????????????????????")
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
    @DisplayName("?????? ?????? - ?????? ?????? : ????????? ????????? ??????")
    void addPetalExceptionWhenNotExistDandelion() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        dandelionRepository.save(dandelion1);
        PetalCreateSvcDto newPetalCreateSvcDto = PetalCreateSvcDto.builder()
                .message("?????????????????????")
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
    @DisplayName("?????? ?????? - ?????? ?????? : ????????? ?????? ??????")
    void addPetalExceptionWhenDeletedDandelion() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        dandelion1.delete();
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        PetalCreateSvcDto newPetalCreateSvcDto = PetalCreateSvcDto.builder()
                .message("?????????????????????")
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
    @DisplayName("?????? ?????? - ??????")
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
                .message("???????????????")
                .imageFile(null)
                .blossomedDate(LocalDate.parse("2022-05-30"))
                .nation("KOREA")
                .build();
        Dandelion savedDandelion = dandelionService.createDandelion(newSavedMember.getSeq(), newDandelion);

        Member savedMember1 = memberRepository.save(member1);
        PetalCreateSvcDto newPetalCreateSvcDto = PetalCreateSvcDto.builder()
                .message("?????????????????????")
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
    @DisplayName("?????? ?????? - ??????(READY ?????? ??????)")
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
                .message("???????????????")
                .imageFile(null)
                .blossomedDate(LocalDate.parse("2022-05-30"))
                .nation("KOREA")
                .build();
        Dandelion savedDandelion = dandelionService.createDandelion(newSavedMember.getSeq(), newDandelion);

        Member savedMember1 = memberRepository.save(member1);
        PetalCreateSvcDto newPetalCreateSvcDto = PetalCreateSvcDto.builder()
                .message("?????????????????????")
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
    @DisplayName("?????? ?????? - ?????? ?????? : ?????? ??????")
    void addPetalExceptionWhenMultiplePetal() throws IOException {
        // given
        Member savedMember1 = memberRepository.save(member1);
        DandelionCreateSvcDto newDandelion = DandelionCreateSvcDto.builder()
                .message("???????????????")
                .imageFile(null)
                .blossomedDate(LocalDate.parse("2022-05-30"))
                .nation("KOREA")
                .build();
        Dandelion savedDandelion = dandelionService.createDandelion(savedMember1.getSeq(), newDandelion);

        PetalCreateSvcDto newPetalCreateSvcDto = PetalCreateSvcDto.builder()
                .message("?????????????????????")
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
    @DisplayName("????????? ???????????? ?????? - ??????(blossomed)")
    void getDandelionDetailSuccessBlossomed() {
        //given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);

        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Petal savedPetal1 = petalRepository.save(Petal.builder()
                .message("???????????????!")
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
                .message("??? ????????????")
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
        assertThat(petalInfos.get(0).getMessage()).isEqualTo("???????????????!");
        assertThat(petalInfos.get(1).getMessage()).isEqualTo("??? ????????????");
    }

    @Test
    @DisplayName("????????? ???????????? ?????? - ??????(album)")
    void getDandelionDetailSuccessAlbum() {
        //given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);

        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Petal savedPetal1 = petalRepository.save(Petal.builder()
                .message("???????????????!")
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
                .message("??? ????????????")
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
        assertThat(petalInfos.get(0).getMessage()).isEqualTo("???????????????!");
        assertThat(petalInfos.get(1).getMessage()).isEqualTo("??? ????????????");
    }

    @Test
    @DisplayName("????????? ???????????? ?????? - ??????(participated)")
    void getDandelionDetailSuccessParticipated() {
        //given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);

        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Petal savedPetal1 = petalRepository.save(Petal.builder()
                .message("???????????????!")
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
                .message("??? ????????????")
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
        assertThat(petalInfos.get(0).getMessage()).isEqualTo("???????????????!");
        assertThat(petalInfos.get(1).getMessage()).isEqualTo("??? ????????????");
    }

    @Test
    @DisplayName("????????? ???????????? ?????? - ??????(?????? ??????X)")
    void getDandelionDetailFailNotExistMember() {
        //given
        Member savedMember1 = memberRepository.save(member1);

        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        petalRepository.save(Petal.builder()
                .message("???????????????!")
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
    @DisplayName("????????? ???????????? ?????? - ??????(?????? deleted)")
    void getDandelionDetailFailDeletedMember() {
        //given
        Member savedMember1 = memberRepository.save(member1);

        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        petalRepository.save(Petal.builder()
                .message("???????????????!")
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
    @DisplayName("????????? ???????????? ?????? - ??????(????????? ??????X)")
    void getDandelionDetailFailNotExistDandelion() {
        //given
        Member savedMember1 = memberRepository.save(member1);

        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        petalRepository.save(Petal.builder()
                .message("???????????????!")
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
    @DisplayName("????????? ???????????? ?????? - ??????(????????? deleted)")
    void getDandelionDetailFailDeletedDandelion() {
        //given
        Member savedMember1 = memberRepository.save(member1);

        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        petalRepository.save(Petal.builder()
                .message("???????????????!")
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
    @DisplayName("?????? ????????? ?????? ????????? - ???????????? ?????? ??????")
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
                    .message("?????????1")
                    .imageFilename("?????????1")
                    .nation("??????1")
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

        //????????? ?????????
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

        //????????? ?????????
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

        // 0??? ????????? ??????
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

        // 3??? ????????? ?????? ??????
        dandelions.get(3).getPetals().get(0).delete();
        petalRepository.save(dandelions.get(3).getPetals().get(0));
        dandelionRepository.save(dandelions.get(3));
        em.flush();
        em.clear();

        participationListPageSvcDto = dandelionService.getParticipationInfo(member2.getSeq(), PageRequest.of(page, size));

        assertThat(participationListPageSvcDto.getDandelionInfos().get(0).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 1");
        assertThat(participationListPageSvcDto.getDandelionInfos().get(1).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 4");
        assertThat(participationListPageSvcDto.getDandelionInfos().get(2).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 6");

        // 4??? ????????? ?????? ??????
        tagRepository.delete(dandelions.get(4).getTags().get(0));
        em.flush();
        em.clear();


        participationListPageSvcDto = dandelionService.getParticipationInfo(member2.getSeq(), PageRequest.of(page, size));

        assertThat(participationListPageSvcDto.getDandelionInfos().get(0).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 1");
        assertThat(participationListPageSvcDto.getDandelionInfos().get(1).getTagInfos().size()).isEqualTo(0);
        assertThat(participationListPageSvcDto.getDandelionInfos().get(2).getTagInfos().get(0).getTagName()).isEqualTo("TagNum = 6");

        // ????????? ?????? ??????

        Member findMember1 = memberRepository.findBySeq(member1.getSeq()).orElse(null);
        findMember1.delete();

        em.flush();
        em.clear();

        participationListPageSvcDto = dandelionService.getParticipationInfo(member2.getSeq(), PageRequest.of(page, size));
        assertThat(participationListPageSvcDto).isNull();
    }

    @Test
    @DisplayName("?????? ????????? ?????? ????????? - ???????????? ?????? ??????")
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
    @DisplayName("????????? ??????(Ready) ?????? - True")
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
    @DisplayName("????????? ??????(Ready) ?????? - False")
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
    @DisplayName("????????? ??????(Ready) ?????? - ?????? ??????")
    void checkReadyException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isReady(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }
}