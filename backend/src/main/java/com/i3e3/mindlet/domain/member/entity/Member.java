package com.i3e3.mindlet.domain.member.entity;

import com.i3e3.mindlet.domain.admin.entity.Report;
import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Petal;
import com.i3e3.mindlet.domain.dandelion.entity.Tag;
import com.i3e3.mindlet.global.entity.base.BaseLastModifiedEntity;
import com.i3e3.mindlet.global.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "tb_member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"seq", "id", "password", "role", "isDeleted"})
public class Member extends BaseLastModifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_seq", columnDefinition = "BIGINT UNSIGNED")
    private Long seq;

    @Column(nullable = false, length = 20)
    private String id;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean isDeleted;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private AppConfig appConfig;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dandelion> dandelions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Petal> petals = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberDandelionHistory> memberDandelionHistories = new ArrayList<>();

    @Builder
    public Member(String id, String password) {
        this.id = id;
        this.password = password;
        this.role = Role.MEMBER;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public void changeRole(Role role) {
        this.role = role;
    }

    public void delete() {
        this.isDeleted = true;

        for (Dandelion dandelion : this.dandelions) {
            dandelion.delete();
        }

        for (Petal petal : this.petals) {
            petal.delete();
        }

        this.tags.clear();
    }
}
