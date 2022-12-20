package com.i3e3.mindlet.domain.admin.entity;

import com.i3e3.mindlet.domain.dandelion.entity.Petal;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.global.entity.base.BaseLastModifiedEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"seq", "reason", "status"})
public class Report extends BaseLastModifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_seq", columnDefinition = "BIGINT UNSIGNED")
    private Long seq;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Reason reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petal_seq", nullable = false)
    private Petal petal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_seq")
    private Admin admin;

    @Builder
    public Report(Reason reason, Member member, Petal petal) {
        this.reason = reason;
        this.status = Status.REQUESTED;
        this.member = member;
        this.petal = petal;

        member.getReports().add(this);
        petal.getReports().add(this);
    }

    public void changeStatus(Status status, Admin admin) {
        this.status = status;

        if (this.admin != null) {
            this.admin.getReports().remove(this);
            this.admin = admin;
            admin.getReports().add(this);
        }
    }

    public enum Reason {

        VIOLENCE, AD, SEXUAL_HARASSMENT, INAPPROPRIATE;
    }

    public enum Status {

        REQUESTED("receive a report"),
        PENDING("reported cumulatively three times and is pending"),
        REJECTED("rejected a report"),
        ACCEPTED("accepted a report");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
