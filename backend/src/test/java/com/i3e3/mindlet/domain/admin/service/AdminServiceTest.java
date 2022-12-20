package com.i3e3.mindlet.domain.admin.service;

import com.i3e3.mindlet.domain.admin.controller.form.RegisterForm;
import com.i3e3.mindlet.domain.admin.entity.Admin;
import com.i3e3.mindlet.domain.admin.entity.RegisterKey;
import com.i3e3.mindlet.domain.admin.exception.DuplicateIdException;
import com.i3e3.mindlet.domain.admin.exception.InvalidKeyException;
import com.i3e3.mindlet.domain.admin.exception.PasswordContainIdException;
import com.i3e3.mindlet.domain.admin.repository.AdminRepository;
import com.i3e3.mindlet.domain.admin.repository.RegisterKeyRepository;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class AdminServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RegisterKeyRepository registerKeyRepository;

    @Autowired
    private AdminService adminService;

    private RegisterForm registerForm1;

    @BeforeEach
    void setUp() {
        adminRepository.deleteAll();
        registerKeyRepository.deleteAll();
        em.flush();
        em.clear();

        registerForm1 = RegisterForm.builder()
                .id("id01")
                .password("password01")
                .key("aaaa-bbbb-cccc-dddd")
                .build();
    }

    @Test
    @DisplayName("관리자 회원가입 - 성공")
    void registerSuccess() {
        /**
         * 임시 키 생성
         */
        registerKeyRepository.save(RegisterKey.builder()
                .value(registerForm1.getKey())
                .build());
        em.flush();
        em.clear();

        /**
         * 회원가입
         */
        Admin savedAdmin = adminService.register(registerForm1.toDto());

        /**
         * 관리자 데이터 조회
         */
        Admin findAdmin = adminRepository.findAdminBySeq(savedAdmin.getSeq())
                .orElse(null);

        /**
         * 데이터 검증
         */
        assertThat(findAdmin.getId()).isEqualTo(registerForm1.getId());
        assertThat(passwordEncoder.matches(registerForm1.getPassword(), findAdmin.getPassword())).isTrue();
    }

    @Test
    @DisplayName("관리자 회원가입 - 실패 : 패스워드에 아이디 포함")
    void registerFailWhenPasswordContainId() {
        /**
         * 임시 키 생성
         */
        String key = "newKey";
        registerKeyRepository.save(RegisterKey.builder()
                .value(key)
                .build());
        em.flush();
        em.clear();

        RegisterForm newRegisterForm = RegisterForm.builder()
                .id("idid")
                .password("idid12#$")
                .key(key)
                .build();

        /**
         * 예외 발생 검증
         */
        assertThatThrownBy(() -> adminService.register(newRegisterForm.toDto()))
                .isInstanceOf(PasswordContainIdException.class)
                .hasMessage(ErrorMessage.PASSWORD_CONTAIN_ID.getMessage());
    }

    @Test
    @DisplayName("관리자 회원가입 실패 - 아이디가 중복된 경우")
    void registerFailWhenDuplicateId() {
        /**
         * 임시 키 생성
         */
        registerKeyRepository.save(RegisterKey.builder()
                .value(registerForm1.getKey())
                .build());
        em.flush();
        em.clear();

        /**
         * 회원가입
         */
        adminService.register(registerForm1.toDto());

        /**
         * 이미 회원가입된 계정의 아이디로 회원가입 시도
         */
        RegisterForm newRegisterForm = RegisterForm.builder()
                .id(registerForm1.getId())
                .password("pass12#$")
                .key("newKey")
                .build();

        /**
         * 예외 발생 검증
         */
        assertThatThrownBy(() -> adminService.register(newRegisterForm.toDto()))
                .isInstanceOf(DuplicateIdException.class)
                .hasMessage(ErrorMessage.DUPLICATE_ID.getMessage());
    }

    @Test
    @DisplayName("관리자 회원가입 실패 - 실패 : 유효하지 않은 키")
    void registerFailWhenInvalidRegisterKey() {
        RegisterForm newRegisterForm = RegisterForm.builder()
                .id("idid2")
                .password("pass12#4")
                .key("invalid-key") // 데이터베이스에 존재하지 않는 키
                .build();

        assertThatThrownBy(() -> adminService.register(newRegisterForm.toDto()))
                .isInstanceOf(InvalidKeyException.class)
                .hasMessage(ErrorMessage.INVALID_REGISTER_KEY.getMessage());
    }
}