package com.i3e3.mindlet.global.util;

import com.i3e3.mindlet.global.auth.CustomUserDetails;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtil {

    public static Long getMemberSeq() {
        return ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getMember().getSeq();
    }

    public static void verifyMember(Long memberSeq) {
        if (((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getMember().getSeq().equals(memberSeq)) {
            return;
        }

        throw new AccessDeniedException(ErrorMessage.INVALID_REQUEST.getMessage());
    }
}
