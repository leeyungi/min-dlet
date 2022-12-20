package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Petal;
import com.i3e3.mindlet.domain.dandelion.entity.Tag;
import com.i3e3.mindlet.domain.dandelion.repository.DandelionRepository;
import com.i3e3.mindlet.domain.dandelion.repository.PetalRepository;
import com.i3e3.mindlet.domain.dandelion.repository.TagRepository;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import com.i3e3.mindlet.global.enums.Community;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TagServiceTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DandelionRepository dandelionRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PetalRepository petalRepository;

    @Autowired
    private TagService tagService;

    private Member member1, member2, member3;

    private Dandelion dandelion1, dandelion2;

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

        dandelion1 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();

        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member2)
                .build();
    }

    @Test
    @DisplayName("민들레 태그 추가 - 성공")
    void registerDandelionTagSuccess() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Member savedMember3 = memberRepository.save(member3);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);

        petalRepository.save(
                Petal.builder()
                        .member(savedMember1)
                        .dandelion(savedDandelion1)
                        .message("안녕하세요!!!")
                        .nation("Korea")
                        .build());
        petalRepository.save(
                Petal.builder()
                        .member(savedMember2)
                        .dandelion(savedDandelion2)
                        .message("Hello~!!!")
                        .nation("Canada")
                        .build());

        petalRepository.save(
                Petal.builder()
                        .member(savedMember3)
                        .dandelion(savedDandelion1)
                        .message("오 한국 사람!!!!")
                        .nation("Korea")
                        .build());

        petalRepository.save(
                Petal.builder()
                        .member(savedMember3)
                        .dandelion(savedDandelion2)
                        .message("오 캐나다 사람!!!")
                        .nation("Korea")
                        .build());

        savedDandelion1.changeStatus(Dandelion.Status.BLOSSOMED);
        savedDandelion2.changeStatus(Dandelion.Status.ALBUM);

        em.flush();
        em.clear();

        // when

        String name1 = "첫 한국인";
        String name2 = "영어공부하자";

        Tag registeredTag1 = tagService.registerDandelionTag(savedDandelion1.getSeq(), savedMember3.getSeq(), name1);
        Tag registeredTag2 = tagService.registerDandelionTag(savedDandelion2.getSeq(), savedMember3.getSeq(), name2);

        Tag findTag1 = tagRepository.findBySeq(registeredTag1.getSeq()).orElse(null);
        Tag findTag2 = tagRepository.findBySeq(registeredTag2.getSeq()).orElse(null);

        // then
        assertThat(findTag1.getName()).isEqualTo(name1);
        assertThat(findTag2.getName()).isEqualTo(name2);
        assertThat(findTag1.getMember().getSeq()).isEqualTo(savedMember3.getSeq());
        assertThat(findTag2.getMember().getSeq()).isEqualTo(savedMember3.getSeq());
        assertThat(findTag1.getDandelion().getSeq()).isEqualTo(savedDandelion1.getSeq());
        assertThat(findTag2.getDandelion().getSeq()).isEqualTo(savedDandelion2.getSeq());
    }

    @Test
    @DisplayName("민들레 태그 추가 - 실패(민들레 존재 X)")
    void registerDandelionTagFailNotExistDandelion() {
        //given
        Member savedMember1 = memberRepository.save(member1);

        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> tagService.registerDandelionTag(0L, savedMember1.getSeq(), "태그 입니다."))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 태그 추가 - 실패(회원 존재 X)")
    void registerDandelionTagFailNotExistMember() {
        //given
        memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> tagService.registerDandelionTag(savedDandelion1.getSeq(), 0L, "태그 입니다."))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 태그 추가 - 실패(민들레 deleted)")
    void registerDandelionTagFailDeletedDandelion() {
        //given
        memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        savedDandelion1.delete();

        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> tagService.registerDandelionTag(savedDandelion1.getSeq(), savedMember2.getSeq(), "태그 입니다."))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 태그 추가 - 실패(회원 deleted)")
    void registerDandelionTagFailDeletedMember() {
        //given
        memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        savedMember2.delete();

        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> tagService.registerDandelionTag(savedDandelion1.getSeq(), savedMember2.getSeq(), "태그 입니다."))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }
}