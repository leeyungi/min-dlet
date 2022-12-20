package com.i3e3.mindlet.global.config.security;

import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.auth.CustomUserDetailService;
import com.i3e3.mindlet.global.auth.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    private final MemberRepository memberRepository;

    private final CustomUserDetailService customUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.customUserDetailService);
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .addFilter(corsFilter)
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), memberRepository))
                .authorizeRequests()
                .anyRequest().permitAll();

//        http
//                .csrf().disable()
//                .headers().frameOptions().disable()
//
//                .and()
//                .authorizeRequests()
////                .antMatchers("/", "/css/**", "/images/**", "/js/**").permitAll()
////                .antMatchers("/error/**").permitAll()
////                .antMatchers("/admin/register", "/admin/login").permitAll()
////                .antMatchers("/admin/**").hasAnyRole("ROLE_ADMIN", "ROLE_SUPER_ADMIN")
////                .anyRequest().authenticated()
//                .antMatchers("/**").permitAll()
//
//                .and()
//                .logout()
//                .logoutSuccessUrl("/login");
    }
}
