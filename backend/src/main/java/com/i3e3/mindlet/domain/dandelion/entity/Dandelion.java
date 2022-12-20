package com.i3e3.mindlet.domain.dandelion.entity;

import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.entity.MemberDandelionHistory;
import com.i3e3.mindlet.global.entity.base.BaseLastModifiedEntity;
import com.i3e3.mindlet.global.enums.Community;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_dandelion")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"seq", "blossomedDate", "community", "flowerSignNumber", "description", "status", "isDeleted"})
public class Dandelion extends BaseLastModifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dandelion_seq", columnDefinition = "BIGINT UNSIGNED")
    private Long seq;

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate blossomedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Community community;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer flowerSignNumber;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "dandelion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Petal> petals = new ArrayList<>();

    @OneToMany(mappedBy = "dandelion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "dandelion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberDandelionHistory> memberDandelionHistories = new ArrayList<>();

    @Builder
    public Dandelion(LocalDate blossomedDate, Community community, Integer flowerSignNumber, Member member) {
        this.blossomedDate = blossomedDate;
        this.community = community;
        this.flowerSignNumber = flowerSignNumber;
        this.status = Status.FLYING;
        this.member = member;

        member.getDandelions().add(this);
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

    public void delete() {
        this.isDeleted = true;

        for (Petal petal : this.getPetals()) {
            petal.delete();
        }

        this.tags.clear();
    }

    public void changeCommunity(Community community) {
        this.community = community;
    }

    public enum Status {

        FLYING("can fly"),
        HOLD("caught by someone"),
        RETURN("returned to owner"),
        PENDING("under confirm"),
        BLOCKED("inappropriate dandelion"),
        READY("can't catch before return"),
        BLOSSOMED("in the garden"),
        ALBUM("in the album");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
