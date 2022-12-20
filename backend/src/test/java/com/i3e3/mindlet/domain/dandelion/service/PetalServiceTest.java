package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.admin.entity.Report;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class PetalServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DandelionRepository dandelionRepository;

    @Autowired
    private PetalRepository petalRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PetalService petalService;

    private Member member1, member2;

    private Dandelion dandelion1;

    private Petal petal1, petal2;

    private Tag tag1, tag2, tag3;

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
    @DisplayName("신고 접수 - 리스트 크기가 0일 경우")
    void addReportRequestedWhenNotingReports() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        Petal savedPetal = petalRepository.save(petal1);
        em.flush();
        em.clear();

        // when
        petalService.reportPetal(member2.getSeq(), savedPetal.getSeq(), Report.Reason.AD);
        Petal findPetal = petalRepository.findBySeq(savedPetal.getSeq())
                .orElse(null);
        List<Report> reports = findPetal.getReports();

        // then
        assertThat(reports.size()).isEqualTo(1);
        assertThat(reports.get(0).getStatus()).isEqualTo(Report.Status.REQUESTED);
        assertThat(reports.get(0).getMember().getSeq()).isEqualTo(member2.getSeq());
        assertThat(reports.get(0).getPetal().getSeq()).isEqualTo(petal1.getSeq());
        assertThat(reports.get(0).getReason()).isEqualTo(Report.Reason.AD);
    }

    @Test
    @DisplayName("신고 접수 - 리스트가 있을 경우 상태가 rejected 일 경우")
    void PassWhenStatusRejected() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        Petal savedPetal = petalRepository.save(petal1);
        Report newReport = Report.builder()
                .reason(Report.Reason.AD)
                .member(member2)
                .petal(petal1)
                .build();
        newReport.changeStatus(Report.Status.REJECTED, null);
        em.flush();
        em.clear();

        // when
        petalService.reportPetal(member2.getSeq(), savedPetal.getSeq(), Report.Reason.AD);
        Petal findPetal = petalRepository.findBySeq(savedPetal.getSeq())
                .orElse(null);
        List<Report> reports = findPetal.getReports();

        // then
        assertThat(reports.size()).isEqualTo(1);
        assertThat(reports.get(0).getStatus()).isEqualTo(Report.Status.REJECTED);
        assertThat(reports.get(0).getMember().getSeq()).isEqualTo(member2.getSeq());
        assertThat(reports.get(0).getPetal().getSeq()).isEqualTo(petal1.getSeq());
        assertThat(reports.get(0).getReason()).isEqualTo(Report.Reason.AD);
    }

    @Test
    @DisplayName("신고 접수 - rejected가 아니고 리스트 크기가 1 이하일 경우")
    void addReportRequestedWhenHasData() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        Petal savedPetal = petalRepository.save(petal1);
        Report newReport = Report.builder()
                .reason(Report.Reason.AD)
                .member(member2)
                .petal(petal1)
                .build();
        newReport.changeStatus(Report.Status.REQUESTED, null);
        em.flush();
        em.clear();

        // when
        petalService.reportPetal(member2.getSeq(), savedPetal.getSeq(), Report.Reason.AD);
        Petal findPetal = petalRepository.findBySeq(savedPetal.getSeq())
                .orElse(null);
        List<Report> reports = findPetal.getReports();

        // then
        assertThat(reports.size()).isEqualTo(2);
        assertThat(reports.get(1).getStatus()).isEqualTo(Report.Status.REQUESTED);
        assertThat(reports.get(1).getMember().getSeq()).isEqualTo(member2.getSeq());
        assertThat(reports.get(1).getPetal().getSeq()).isEqualTo(petal1.getSeq());
        assertThat(reports.get(1).getReason()).isEqualTo(Report.Reason.AD);
    }

    @Test
    @DisplayName("신고 접수 - rejected가 아니고 리스트 크기가 2 이상일 경우")
    void addReportPendingWhenHasData() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        Petal savedPetal = petalRepository.save(petal1);
        Report newReport1 = Report.builder()
                .reason(Report.Reason.AD)
                .member(member2)
                .petal(petal1)
                .build();
        newReport1.changeStatus(Report.Status.REQUESTED, null);
        Report newReport2 = Report.builder()
                .reason(Report.Reason.AD)
                .member(member2)
                .petal(petal1)
                .build();
        newReport2.changeStatus(Report.Status.REQUESTED, null);
        em.flush();
        em.clear();

        // when
        petalService.reportPetal(member2.getSeq(), savedPetal.getSeq(), Report.Reason.AD);
        Petal findPetal = petalRepository.findBySeq(savedPetal.getSeq())
                .orElse(null);
        List<Report> reports = findPetal.getReports();

        // then
        assertThat(reports.size()).isEqualTo(3);
        assertThat(reports.get(0).getStatus()).isEqualTo(Report.Status.PENDING);
        assertThat(reports.get(1).getStatus()).isEqualTo(Report.Status.PENDING);
        assertThat(reports.get(2).getStatus()).isEqualTo(Report.Status.PENDING);
    }

    @Test
    @DisplayName("꽃잎의 민들레 주인 확인 - 주인이 맞을경우")
    void checkDandelionOwnerContainPetalTrue() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        petal2 = Petal.builder()
                .message("hello")
                .imageFilename("imageFilename")
                .nation("KOREA")
                .dandelion(dandelion1)
                .member(member2)
                .build();
        petalRepository.save(petal1);
        petalRepository.save(petal2);

        //when
        boolean isOwner = petalService.isDandelionOwnerByPetal(member1.getSeq(), petal2.getSeq());

        //then
        assertThat(isOwner).isTrue();
    }

    @Test
    @DisplayName("꽃잎의 민들레 주인 확인 - 주인이 아닐경우")
    void checkDandelionOwnerContainPetalFalse() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        petal2 = Petal.builder()
                .message("hello")
                .imageFilename("imageFilename")
                .nation("KOREA")
                .dandelion(dandelion1)
                .member(member2)
                .build();
        petalRepository.save(petal1);
        petalRepository.save(petal2);

        //when
        boolean isOwner = petalService.isDandelionOwnerByPetal(member2.getSeq(), petal2.getSeq());

        //then
        assertThat(isOwner).isFalse();
    }

    @Test
    @DisplayName("꽃잎의 민들레 주인 확인 - 없는 꽃잎일 경우")
    void checkDandelionOwnerNotExistPetal() {
        // given
        memberRepository.save(member1);

        //when

        //then
        assertThatThrownBy(() -> petalService.isDandelionOwnerByPetal(member1.getSeq(), 0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("꽃잎의 민들레 주인 확인 - 삭제된 꽃잎일 경우")
    void checkDandelionOwnerPetalIsDeleted() {
        // given
        memberRepository.save(member1);
        petal1.delete();
        petalRepository.save(petal1);

        //when

        //then
        assertThatThrownBy(() -> petalService.isDandelionOwnerByPetal(member1.getSeq(), petal1.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("꽃잎 삭제 - 성공")
    void deletePetalSuccess() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        petal2 = Petal.builder()
                .message("hello")
                .imageFilename("imageFilename")
                .nation("KOREA")
                .dandelion(dandelion1)
                .member(member2)
                .build();
        petalRepository.save(petal1);
        petalRepository.save(petal2);

        tag1 = Tag.builder()
                .name("first Tag")
                .dandelion(dandelion1)
                .member(member2)
                .build();

        tag2 = Tag.builder()
                .name("second Tag")
                .dandelion(dandelion1)
                .member(member2)
                .build();

        tag3 = Tag.builder()
                .name("third Tag")
                .dandelion(dandelion1)
                .member(member2)
                .build();

        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);
        em.flush();
        em.clear();

        //when

        petalService.deletePetal(petal2.getSeq());

        Petal petal = petalRepository.findBySeq(petal2.getSeq())
                .orElse(null);

        List<Tag> tags = tagRepository.findTagListByMemberSeqAndDandelionSeq(petal2.getMember().getSeq(), dandelion1.getSeq())
                .orElse(null);

        //then
        assertThat(petal).isNull();
        assertThat(tags.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("꽃잎 삭제 - 없는 꽃잎일 경우")
    void deletePetalNotExist() {
        // given

        //when

        //then
        assertThatThrownBy(() -> petalService.deletePetal(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("꽃잎 삭제 - 이미 삭제된 꽃잎일 경우")
    void deletePetalIsDeleted() {
        // given
        memberRepository.save(member1);
        dandelionRepository.save(dandelion1);
        petal1.delete();
        petalRepository.save(petal1);
        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> petalService.deletePetal(petal1.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }
}
