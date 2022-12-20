package com.i3e3.mindlet.global.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    MEMBER("ROLE_MEMBER", "ordinary member"),
    ADMIN("ROLE_ADMIN", "admin"),
    SUPER_ADMIN("ROLE_SUPER_ADMIN", "super admin");

    private final String authority;

    private final String description;

    Role(String authority, String description) {
        this.authority = authority;
        this.description = description;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public String getDescription() {
        return description;
    }
}
