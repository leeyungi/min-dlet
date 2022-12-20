package com.i3e3.mindlet.domain.member.entity;

import com.i3e3.mindlet.global.entity.base.BaseLastModifiedEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"seq", "message", "type", "isChecked"})
public class Notification extends BaseLastModifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_seq", columnDefinition = "BIGINT UNSIGNED")
    private Long seq;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean isChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq", nullable = false)
    private Member member;

    @Builder
    public Notification(String message, Type type, Member member) {
        this.message = message;
        this.type = type;
        this.member = member;

        member.getNotifications().add(this);
    }

    public void check() {
        this.isChecked = true;
    }

    public enum Type {

        NEW_DANDELION_ARRIVED("new dandelion seed has arrived"),
        PETAL_REPORTED_AND_DELETED("petal was reported and deleted"),
        PETAL_DELETED_BY_OWNER("owner deleted your petal"),
        DANDELION_REPORTED_AND_DELETED("dandelion was reported and deleted"),
        DANDELION_REPORTED_AND_REJECT("dandelion was reported but rejected");

        private final String description;

        Type(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
