package com.i3e3.mindlet.domain.dandelion.entity;

import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.global.entity.base.BaseCreatedEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"seq", "name"})
public class Tag extends BaseCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_seq", columnDefinition = "BIGINT UNSIGNED")
    private Long seq;

    @Column(nullable = false, length = 20)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dandelion_seq", nullable = false)
    private Dandelion dandelion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq", nullable = false)
    private Member member;

    @Builder
    public Tag(String name, Dandelion dandelion, Member member) {
        this.name = name;
        this.dandelion = dandelion;
        this.member = member;

        dandelion.getTags().add(this);
        member.getTags().add(this);
    }
}
