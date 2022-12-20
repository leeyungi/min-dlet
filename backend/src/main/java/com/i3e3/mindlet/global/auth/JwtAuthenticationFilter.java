package com.i3e3.mindlet.global.auth;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import com.i3e3.mindlet.global.util.JwtTokenUtil;
import com.i3e3.mindlet.global.util.ResponseBodyWriteUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private MemberRepository memberRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(JwtTokenUtil.HEADER_STRING);
        if (header == null || !header.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authentication = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            ResponseBodyWriteUtil.sendError(request, response, ex);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Transactional(readOnly = true)
    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtTokenUtil.HEADER_STRING);
        if (token != null) {
            JWTVerifier verifier = JwtTokenUtil.getVerifier();
            JwtTokenUtil.handleError(token);
            DecodedJWT decodedJWT = verifier.verify(token.replace(JwtTokenUtil.TOKEN_PREFIX, ""));
            Long seq = decodedJWT.getClaim("seq").asLong();

            if (seq != null) {
                Member findMember = memberRepository.findBySeq(seq)
                        .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_ID.getMessage()));

                if (findMember != null) {
                    CustomUserDetails customUserDetails = new CustomUserDetails(findMember);
                    customUserDetails.setAuthorities(Collections.singletonList(findMember.getRole()));
                    UsernamePasswordAuthenticationToken jwtAuthentication =
                            new UsernamePasswordAuthenticationToken(seq, null, customUserDetails.getAuthorities());
                    jwtAuthentication.setDetails(customUserDetails);
                    return jwtAuthentication;
                }
            }
            return null;
        }
        return null;
    }
}
