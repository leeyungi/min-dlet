package com.i3e3.mindlet.domain.admin.entity;

import com.i3e3.mindlet.global.entity.base.BaseLastModifiedEntity;
import com.i3e3.mindlet.global.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "tb_admin",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id"),
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"seq", "id", "password", "role", "isDeleted"})
public class Admin extends BaseLastModifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_seq", columnDefinition = "BIGINT UNSIGNED")
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

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @Builder
    public Admin(String id, String password, Role role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeRole(Role role) {
        this.role = role;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
