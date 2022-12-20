package com.i3e3.mindlet.domain.dandelion.repository;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Petal;
import com.i3e3.mindlet.domain.dandelion.entity.Tag;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TagRepositoryTest {

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

    private Member member1, member2;

    private Dandelion dandelion1, dandelion2;

    private Petal petal1, petal2, petal3;

    private Tag tag1, tag2, tag3;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        dandelionRepository.deleteAll();
        petalRepository.deleteAll();
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

        em.persist(member1);
        em.persist(member2);

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

        em.persist(dandelion1);
        em.persist(dandelion2);

        petal1 = Petal.builder()
                .message("hello")
                .imageFilename("imageFilename")
                .nation("KOREA")
                .dandelion(dandelion1)
                .member(member1)
                .build();

        petal2 = Petal.builder()
                .message("hello")
                .imageFilename("imageFilename")
                .nation("KOREA")
                .dandelion(dandelion2)
                .member(member2)
                .build();

        petal3 = Petal.builder()
                .message("hello")
                .imageFilename("imageFilename")
                .nation("KOREA")
                .dandelion(dandelion1)
                .member(member2)
                .build();

        em.persist(petal1);
        em.persist(petal2);
        em.persist(petal3);

        tag1 = Tag.builder()
                .name("first Tag")
                .dandelion(dandelion1)
                .member(member2)
                .build();

        tag2 = Tag.builder()
                .name("second Tag")
                .dandelion(dandelion2)
                .member(member1)
                .build();

        tag3 = Tag.builder()
                .name("third Tag")
                .dandelion(dandelion1)
                .member(member2)
                .build();

        em.persist(tag1);
        em.persist(tag2);
        em.persist(tag3);
    }

    @Test
    @DisplayName("태그 리스트 가져오기 - 데이터가 있는경우")
    void getTagListHasData() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        dandelionRepository.save(dandelion2);

        petalRepository.save(petal1);
        petalRepository.save(petal2);
        petalRepository.save(petal3);

        Tag savedTag1 = tagRepository.save(tag1);
        Tag savedTag2 = tagRepository.save(tag2);
        Tag savedTag3 = tagRepository.save(tag3);
        em.flush();
        em.clear();

        //when

        List<Tag> tags = tagRepository.findTagListByMemberSeqAndDandelionSeq(member2.getSeq(), dandelion1.getSeq())
                .orElse(null);

        //then
        assertThat(tags.size()).isEqualTo(2);
        assertThat(savedTag1.getName()).isEqualTo(tags.get(0).getName());
        assertThat(savedTag3.getName()).isEqualTo(tags.get(1).getName());
    }

    @Test
    @DisplayName("태그 리스트 가져오기 - 데이터가 없는경우")
    void getTagListNoneData() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        dandelionRepository.save(dandelion2);

        petalRepository.save(petal1);
        petalRepository.save(petal2);
        petalRepository.save(petal3);

        em.flush();
        em.clear();

        tagRepository.delete(tag1);
        tagRepository.delete(tag2);
        tagRepository.delete(tag3);

        em.flush();
        em.clear();

        //when

        List<Tag> tags = tagRepository.findTagListByMemberSeqAndDandelionSeq(member2.getSeq(), dandelion1.getSeq())
                .orElse(null);

        //then
        assertThat(tags.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("멤버 식별키만 사용해서 태그 리스트 가져오기 - 데이터가 있는경우")
    void getTagListUsingOnlyMemberSeqHasData() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        dandelionRepository.save(dandelion2);

        petalRepository.save(petal1);
        petalRepository.save(petal2);
        petalRepository.save(petal3);

        Tag savedTag1 = tagRepository.save(tag1);
        Tag savedTag2 = tagRepository.save(tag2);
        Tag savedTag3 = tagRepository.save(tag3);
        em.flush();
        em.clear();

        //when

        List<Tag> tags = tagRepository.findTagListByMemberSeq(member2.getSeq())
                .orElse(null);

        //then
        assertThat(tags.size()).isEqualTo(2);
        assertThat(savedTag1.getName()).isEqualTo(tags.get(0).getName());
        assertThat(savedTag3.getName()).isEqualTo(tags.get(1).getName());
    }


    @Test
    @DisplayName("멤버 식별키만 사용해서 태그 리스트 가져오기 - 데이터가 없는경우")
    void getTagListUsingOnlyMemberNoneData() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        dandelionRepository.save(dandelion2);

        petalRepository.save(petal1);
        petalRepository.save(petal2);
        petalRepository.save(petal3);

        em.flush();
        em.clear();

        tagRepository.delete(tag1);
        tagRepository.delete(tag2);
        tagRepository.delete(tag3);

        em.flush();
        em.clear();

        //when

        List<Tag> tags = tagRepository.findTagListByMemberSeq(member2.getSeq())
                .orElse(null);

        //then
        assertThat(tags.size()).isEqualTo(0);
    }
}
