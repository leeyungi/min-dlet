package com.i3e3.mindlet.domain.admin.entity;

import com.i3e3.mindlet.global.entity.base.BaseCreatedEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(
        name = "tb_register_key",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "value")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"seq", "value"})
public class RegisterKey extends BaseCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "register_key_seq", columnDefinition = "BIGINT UNSIGNED")
    private Long seq;

    @Column(nullable = false)
    private String value;

    @Builder
    public RegisterKey(String value) {
        this.value = value;
    }
}
