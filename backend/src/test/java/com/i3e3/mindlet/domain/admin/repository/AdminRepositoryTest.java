package com.i3e3.mindlet.domain.admin.repository;

import com.i3e3.mindlet.domain.admin.entity.Admin;
import com.i3e3.mindlet.global.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AdminRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private AdminRepository adminRepository;

    private Admin admin1;

    @BeforeEach
    void setUp() {
        adminRepository.deleteAll();
        em.flush();
        em.clear();

        admin1 = Admin.builder()
                .id("id01")
                .password("pass12#$")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    @DisplayName("관리자 엔티티 조회 by 식별키 - 성공")
    void findAdminBySeqSuccess() {
        /**
         * 엔티티 저장 후 영속성 컨텍스트 초기화
         */
        Admin savedAdmin = adminRepository.save(admin1);
        em.flush();
        em.clear();

        /**
         * 데이터 조회
         */
        Admin findAdmin = adminRepository.findAdminBySeq(savedAdmin.getSeq())
                .orElse(null);

        /**
         * 데이터 검증
         */
        assertThat(findAdmin).isNotNull();
        assertThat(findAdmin.getId()).isEqualTo(admin1.getId());
        assertThat(findAdmin.getPassword()).isEqualTo(admin1.getPassword());
    }

    @Test
    @DisplayName("관리자 엔티티 조회 by 식별키 - 성공 : 데이터기 없는 경우")
    void findAdminBySuccessAndNull() {
        assertThat(adminRepository.findAdminBySeq(0L).isPresent()).isFalse();
    }
}