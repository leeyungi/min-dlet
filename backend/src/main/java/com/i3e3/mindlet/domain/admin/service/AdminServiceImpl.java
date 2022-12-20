package com.i3e3.mindlet.domain.admin.service;

import com.i3e3.mindlet.domain.admin.entity.Admin;
import com.i3e3.mindlet.domain.admin.exception.DuplicateIdException;
import com.i3e3.mindlet.domain.admin.exception.InvalidKeyException;
import com.i3e3.mindlet.domain.admin.exception.PasswordContainIdException;
import com.i3e3.mindlet.domain.admin.repository.AdminRepository;
import com.i3e3.mindlet.domain.admin.repository.RegisterKeyRepository;
import com.i3e3.mindlet.domain.admin.service.dto.AdminRegisterDto;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import com.i3e3.mindlet.global.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final RegisterKeyRepository registerKeyRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Admin register(AdminRegisterDto adminRegisterDto) {
        String id = adminRegisterDto.getId();
        String password = adminRegisterDto.getPassword();
        if (password.contains(id)) {
            throw new PasswordContainIdException(ErrorMessage.PASSWORD_CONTAIN_ID.getMessage());
        } else if (adminRepository.existsById(id)) {
            throw new DuplicateIdException(ErrorMessage.DUPLICATE_ID.getMessage());
        } else if (!registerKeyRepository.existsByValue(adminRegisterDto.getKey())) {
            throw new InvalidKeyException(ErrorMessage.INVALID_REGISTER_KEY.getMessage());
        }

        return adminRepository.save(Admin.builder()
                .id(id)
                .password(passwordEncoder.encode(password))
                .role(Role.ADMIN)
                .build());
    }
}
