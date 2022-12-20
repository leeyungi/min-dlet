package com.i3e3.mindlet.global.auth;

import com.i3e3.mindlet.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member;

    boolean accountNonExpired;

    boolean accountNonLocked;

    boolean credentialNonExpired;

    boolean enabled = false;

    List<GrantedAuthority> roles = new ArrayList<>();

    public Member getMember() {
        return this.member;
    }

    @Override
    public String getUsername() {
        return this.member.getId();
    }

    @Override
    public String getPassword() {
        return this.member.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    public void setAuthorities(List<GrantedAuthority> roles) {
        this.roles = roles;
    }
}
