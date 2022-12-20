package com.i3e3.mindlet.domain.member.repository;

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
class MemberRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    private Member member1;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        em.flush();
        em.clear();

        member1 = Member.builder()
                .id("아이디1")
                .password("패스워드1")
                .build();
    }

    @Test
    @DisplayName("회원 식별키로 회원 데이터 유무 확인 - 데이터가 있는 경우")
    void checkMemberExistSuccess() {
        // given
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        boolean isExist = memberRepository.existsBySeq(savedMember.getSeq());

        // then
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("회원 식별키로 회원 데이터 유무 확인 - 데이터가 없는 경우")
    void checkMemberNoneExist() {
        // given

        // when
        boolean isExist = memberRepository.existsBySeq(0L);

        // then
        assertThat(isExist).isFalse();
    }

    @Test
    @DisplayName("회원 식별키로 회원 데이터 유무 확인 - 데이터가 있지만 탈퇴한 경우")
    void checkMemberExistWhenDeleted() {
        // given
        Member savedMember = memberRepository.save(member1);
        savedMember.delete();
        em.flush();
        em.clear();

        // when
        boolean isExist = memberRepository.existsBySeq(savedMember.getSeq());

        // then
        assertThat(isExist).isFalse();
    }

    @Test
    @DisplayName("아이디로 회원 데이터 유무 확인 - 데이터가 없는 경우")
    void checkMemberExistByIdNotExist() {
        // given

        // when
        boolean isExists = memberRepository.existsByIdContainsDeleted("no1234");

        // then
        assertThat(isExists).isFalse();
    }

    @Test
    @DisplayName("아이디로 회원 데이터 유무 확인 - 데이터가 있는 경우")
    void checkMemberExistById() {
        // given
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        boolean isExists = memberRepository.existsByIdContainsDeleted(savedMember.getId());

        // then
        assertThat(isExists).isTrue();
    }

    @Test
    @DisplayName("회원 식별키로 회원 엔티티 조회 - 데이터가 있는 경우")
    void findMemberBySeq() {
        // given
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findBySeq(savedMember.getSeq())
                .orElse(null);

        // then
        assertThat(findMember.getId()).isEqualTo(member1.getId());
        assertThat(findMember.getPassword()).isEqualTo(member1.getPassword());
    }

    @Test
    @DisplayName("회원 식별키로 회원 엔티티 조회 - 데이터가 있지만 삭제 처리된 경우")
    void findMemberBySeqWhenDeleted() {
        // given
        member1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findBySeq(savedMember.getSeq())
                .orElse(null);

        // then
        assertThat(findMember).isNull();
    }

    @Test
    @DisplayName("회원 식별키로 회원 엔티티 조회 - 데이터가 없는 경우")
    void findMemberBySeqWhenNotExist() {
        // given

        // when
        Member findMember = memberRepository.findBySeq(0L)
                .orElse(null);

        // then
        assertThat(findMember).isNull();
    }

    @Test
    @DisplayName("회원 아이디로 회원 엔티티 조회 - 데이터가 있는 경우")
    void findMemberById() {
        // given
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findById(savedMember.getId())
                .orElse(null);

        // then
        assertThat(findMember.getId()).isEqualTo(member1.getId());
        assertThat(findMember.getPassword()).isEqualTo(member1.getPassword());
    }

    @Test
    @DisplayName("회원 아이디로 회원 엔티티 조회 - 데이터가 있지만 삭제 처리된 경우")
    void findMemberByIdWhenDeleted() {
        // given
        member1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findById(savedMember.getId())
                .orElse(null);

        // then
        assertThat(findMember).isNull();
    }

    @Test
    @DisplayName("회원 아이디로 회원 엔티티 조회 - 데이터가 없는 경우")
    void findMemberByIdWhenNotExist() {
        // given

        // when
        Member findMember = memberRepository.findById("id01")
                .orElse(null);

        // then
        assertThat(findMember).isNull();
    }

    @Test
    @DisplayName("회원 식별키로 회원 엔티티(삭제 포함) 조회 - 데이터가 없는 경우")
    void findMemberBySeqContainsDeletedWhenNotExist() {
        // given

        // when
        Member findMember1 = memberRepository.findBySeqContainsDeleted(0L)
                .orElse(null);

        // then
        assertThat(findMember1).isNull();
    }

    @Test
    @DisplayName("회원 식별키로 회원 엔티티(삭제 포함) 조회 - 데이터가 있는 경우")
    void findMemberBySeqContainsDeletedWhenExist() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        Member findMember1 = memberRepository.findBySeqContainsDeleted(savedMember1.getSeq())
                .orElse(null);

        // then
        assertThat(findMember1.getSeq()).isEqualTo(savedMember1.getSeq());
        assertThat(findMember1.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("회원 식별키로 회원 엔티티(삭제 포함) 조회 - 데이터가 있고 삭제된 경우")
    void findMemberBySeqContainsDeletedWhenDeleted() {
        // given
        member1.delete();
        Member savedMember1 = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        Member findMember1 = memberRepository.findBySeqContainsDeleted(savedMember1.getSeq())
                .orElse(null);

        // then
        assertThat(findMember1.getSeq()).isEqualTo(savedMember1.getSeq());
        assertThat(findMember1.isDeleted()).isTrue();
    }
}
