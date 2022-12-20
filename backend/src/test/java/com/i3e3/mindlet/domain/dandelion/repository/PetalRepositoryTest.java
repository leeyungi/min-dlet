package com.i3e3.mindlet.domain.dandelion.repository;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Petal;
import com.i3e3.mindlet.domain.dandelion.service.DandelionService;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
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

@SpringBootTest
@Transactional
public class PetalRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DandelionRepository dandelionRepository;

    @Autowired
    private PetalRepository petalRepository;

    @Autowired
    private DandelionService dandelionService;

    private Member member1, member2, member3;

    private Dandelion dandelion1;

    private Petal petal1;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        dandelionRepository.deleteAll();
        petalRepository.deleteAll();
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

        petal1 = Petal.builder()
                .message("hello")
                .imageFilename("imageFilename")
                .nation("KOREA")
                .dandelion(dandelion1)
                .member(member1)
                .build();
    }

    @Test
    @DisplayName("꽃잎 식별키로 꽃잎 데이터 조회 - 데이터가 존재 할 경우")
    void findPetalBySeqSuccess() {
        // given
        memberRepository.save(member1);
        dandelionRepository.save(dandelion1);
        Petal savedPetal = petalRepository.save(petal1);
        em.flush();
        em.clear();

        // when
        Petal findPetal = petalRepository.findBySeq(savedPetal.getSeq())
                .orElse(null);

        // then
        assertThat(findPetal).isNotNull();
        assertThat(findPetal.getMessage()).isEqualTo(petal1.getMessage());
        assertThat(findPetal.getImageFilename()).isEqualTo(petal1.getImageFilename());
        assertThat(findPetal.getNation()).isEqualTo(petal1.getNation());
        assertThat(findPetal.getMember().getSeq()).isEqualTo(petal1.getMember().getSeq());
        assertThat(findPetal.getDandelion().getSeq()).isEqualTo(petal1.getDandelion().getSeq());
    }

    @Test
    @DisplayName("꽃잎 식별키로 꽃잎 데이터 조회 - 없는 꽃잎 식별키를 입력했을 경우")
    void findPetalBySeqNotExist() {
        // given

        // when
        Petal findPetal = petalRepository.findBySeq(0L)
                .orElse(null);

        // then
        assertThat(findPetal).isNull();
    }

    @Test
    @DisplayName("꽃잎 식별키로 꽃잎 데이터 조회 - 꽃잎이 삭제 된 경우")
    void findPetalBySeqDelete() {
        // given
        memberRepository.save(member1);
        dandelionRepository.save(dandelion1);
        petal1.delete();
        Petal savedPetal = petalRepository.save(petal1);
        em.flush();
        em.clear();

        // when
        Petal findPetal = petalRepository.findBySeq(savedPetal.getSeq())
                .orElse(null);

        // then
        assertThat(findPetal).isNull();
    }

    @Test
    @DisplayName("꽃잎 식별키로 민들레 데이터 조회 - 존재 할 경우")
    void findDandelionByPetalSeqSuccess() {
        // given
        memberRepository.save(member1);
        dandelionRepository.save(dandelion1);
        Petal savedPetal = petalRepository.save(petal1);
        em.flush();
        em.clear();

        // when
        Dandelion findDandelion = petalRepository.findDandelionBySeq(savedPetal.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion).isNotNull();
        assertThat(findDandelion.getBlossomedDate()).isEqualTo(dandelion1.getBlossomedDate());
        assertThat(findDandelion.getCommunity()).isEqualTo(dandelion1.getCommunity());
        assertThat(findDandelion.getFlowerSignNumber()).isEqualTo(dandelion1.getFlowerSignNumber());
        assertThat(findDandelion.getMember().getSeq()).isEqualTo(dandelion1.getMember().getSeq());
    }

    @Test
    @DisplayName("꽃잎 식별키로 민들레 데이터 조회 - 없는 꽃잎 식별키를 입력했을 경우")
    void findDandelionByPetalSeqNotExist() {
        // given

        // when
        Dandelion findDandelion = petalRepository.findDandelionBySeq(0L)
                .orElse(null);

        // then
        assertThat(findDandelion).isNull();
    }

    @Test
    @DisplayName("꽃잎 식별키로 민들레 데이터 조회 - 꽃잎이 삭제 된 경우")
    void findDandelionByPetalSeqDelete() {
        // given
        memberRepository.save(member1);
        dandelionRepository.save(dandelion1);
        petal1.delete();
        Petal savedPetal = petalRepository.save(petal1);
        em.flush();
        em.clear();

        // when
        Dandelion findDandelion = petalRepository.findDandelionBySeq(savedPetal.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion).isNull();
    }

    @Test
    @DisplayName("민들레 식별키와 회원 식별키로 꽃잎 조회 - 성공")
    void existPetalByDandelionSeqAndMemberSeqTrue() {
        //given
        memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        petalRepository.save(petal1);

        petalRepository.save(Petal.builder()
                .message("hello2")
                .imageFilename("imageFilename")
                .nation("CANADA")
                .dandelion(savedDandelion1)
                .member(savedMember2)
                .build());

        em.flush();
        em.clear();

        //when

        boolean isParticipated = petalRepository.existsPetalByDandelionSeqAndMemberSeq(savedDandelion1.getSeq(), savedMember2.getSeq());

        //then
        assertThat(isParticipated).isTrue();

    }

    @Test
    @DisplayName("민들레 식별키와 회원 식별키로 꽃잎 조회 - 실패(petal 이 deleted)")
    void existPetalByDandelionSeqAndMemberSeqFalseNotExistPetal() {
        //given
        memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        petalRepository.save(petal1);

        Petal savedPetal2 = petalRepository.save(Petal.builder()
                .message("hello2")
                .imageFilename("imageFilename")
                .nation("CANADA")
                .dandelion(savedDandelion1)
                .member(savedMember2)
                .build());

        savedPetal2.delete();

        em.flush();
        em.clear();

        //when

        boolean isParticipated = petalRepository.existsPetalByDandelionSeqAndMemberSeq(savedDandelion1.getSeq(), savedMember2.getSeq());

        //then
        assertThat(isParticipated).isFalse();
    }

    @Test
    @DisplayName("민들레 식별키와 회원 식별키로 꽃잎 조회 - 실패(민들레가 존재 X)")
    void existPetalByDandelionSeqAndMemberSeqFalseNotExistDandelion() {
        //given
        memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        petalRepository.save(petal1);

        em.flush();
        em.clear();

        //when

        boolean isParticipated = petalRepository.existsPetalByDandelionSeqAndMemberSeq(0L, savedMember2.getSeq());

        //then
        assertThat(isParticipated).isFalse();
    }

    @Test
    @DisplayName("민들레 식별키와 회원 식별키로 꽃잎 조회 - 실패(민들레가 deleted)")
    void existPetalByDandelionSeqAndMemberSeqFalseDeletedDandelion() {
        //given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Member savedMember3 = memberRepository.save(member3);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        petalRepository.save(petal1);

        petalRepository.save(Petal.builder()
                .message("hello2")
                .imageFilename("imageFilename")
                .nation("CANADA")
                .dandelion(savedDandelion1)
                .member(savedMember2)
                .build());

        petalRepository.save(Petal.builder()
                .message("호우 샷")
                .imageFilename("/test/img1.jpg")
                .nation("ENGLAND")
                .dandelion(savedDandelion1)
                .member(savedMember3)
                .build());

        em.flush();
        em.clear();

        //when
        dandelionService.deleteDandelion(savedDandelion1.getSeq(), savedMember1.getSeq());
        boolean isParticipated2 = petalRepository.existsPetalByDandelionSeqAndMemberSeq(savedDandelion1.getSeq(), savedMember2.getSeq());
        boolean isParticipated3 = petalRepository.existsPetalByDandelionSeqAndMemberSeq(savedDandelion1.getSeq(), savedMember3.getSeq());

        //then
        assertThat(isParticipated2).isFalse();
        assertThat(isParticipated3).isFalse();
    }

    @Test
    @DisplayName("민들레 식별키와 회원 식별키로 꽃잎 조회 - 실패(회원이 존재 X)")
    void existPetalByDandelionSeqAndMemberSeqFalseNotExistMember() {
        //given
        memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        petalRepository.save(petal1);

        em.flush();
        em.clear();

        //when

        boolean isParticipated = petalRepository.existsPetalByDandelionSeqAndMemberSeq(savedDandelion1.getSeq(), 0L);

        //then
        assertThat(isParticipated).isFalse();
    }

    @Test
    @DisplayName("민들레 식별키와 회원 식별키로 꽃잎 조회 - 실패(회원이 deleted)")
    void existPetalByDandelionSeqAndMemberSeqFalseDeletedMember() {
        //given
        memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        petalRepository.save(petal1);

        petalRepository.save(Petal.builder()
                .message("hello2")
                .imageFilename("imageFilename")
                .nation("CANADA")
                .dandelion(savedDandelion1)
                .member(savedMember2)
                .build());

        savedMember2.delete();
        savedMember2.getPetals().forEach(petal -> petal.delete());

        em.flush();
        em.clear();

        //when

        boolean isParticipated = petalRepository.existsPetalByDandelionSeqAndMemberSeq(savedDandelion1.getSeq(), savedMember2.getSeq());

        //then
        assertThat(isParticipated).isFalse();
    }
}
