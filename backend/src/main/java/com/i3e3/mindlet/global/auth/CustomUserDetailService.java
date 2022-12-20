package com.i3e3.mindlet.global.auth;

import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_ID.getMessage()));

        if (findMember != null) {
            return new CustomUserDetails(findMember);
        }

        return null;
    }
}
