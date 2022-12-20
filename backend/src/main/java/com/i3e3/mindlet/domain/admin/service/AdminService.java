package com.i3e3.mindlet.domain.admin.service;

import com.i3e3.mindlet.domain.admin.entity.Admin;
import com.i3e3.mindlet.domain.admin.service.dto.AdminRegisterDto;

public interface AdminService {

    Admin register(AdminRegisterDto adminRegisterDto);
}
