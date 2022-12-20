package com.i3e3.mindlet.domain.member.entity;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.global.entity.base.BaseCreatedEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_member_dandelion_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@IdClass(MemberDandelionHistorySeq.class)
public class MemberDandelionHistory extends BaseCreatedEntity {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_seq", columnDefinition = "BIGINT UNSIGNED")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dandelion_seq", columnDefinition = "BIGINT UNSIGNED")
    private Dandelion dandelion;

    @Builder
    public MemberDandelionHistory(Member member, Dandelion dandelion) {
        this.member = member;
        this.dandelion = dandelion;

        member.getMemberDandelionHistories().add(this);
        dandelion.getMemberDandelionHistories().add(this);
    }
}
