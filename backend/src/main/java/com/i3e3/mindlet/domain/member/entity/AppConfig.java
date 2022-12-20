package com.i3e3.mindlet.domain.member.entity;

import com.i3e3.mindlet.global.entity.base.BaseLastModifiedEntity;
import com.i3e3.mindlet.global.enums.Community;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_app_config")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"seq", "language", "soundOff", "community"})
public class AppConfig extends BaseLastModifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_config_seq", columnDefinition = "BIGINT UNSIGNED")
    private Long seq;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean soundOff;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Community community;

    @OneToOne
    @JoinColumn(name = "member_seq", nullable = false)
    private Member member;

    @Builder
    public AppConfig(Language language, Member member) {
        this.language = language;
        this.community = Community.KOREA;
        this.member = member;

        member.changeAppConfig(this);
    }

    public void changeLanguage(Language language) {
        this.language = language;
    }

    public void soundOn() {
        this.soundOff = false;
    }

    public void soundOff() {
        this.soundOff = true;
    }

    public void changeCommunity(Community community) {
        this.community = community;
    }

    public enum Language {

        KOREAN("korean"),
        ENGLISH("english");

        private final String description;

        Language(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
