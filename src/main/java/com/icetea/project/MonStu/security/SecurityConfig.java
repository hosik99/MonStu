package com.icetea.project.MonStu.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] passPage = {"/**"};
    private final String[] authenticatedPage = {"/member/**"};

    @Autowired
    private SecurityUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService);    //security의 userDetailsService설정

        http
                //CSRF설정
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) //HttpOnly(true)설정된 쿠키는 javascript에서 접근 못함
//                )
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                .requestMatchers(passPage).permitAll()
//                                .requestMatchers(authenticatedPage).authenticated()  //authenticated() -> 인증된 사용자만
//                                .requestMatchers("/admin/**").hasRole("ADMIN")  //DB에 ROLE_ADMIN으로 저장됨
                )

                .formLogin(formLogin -> formLogin
//                        .loginPage("/login")  //미설정 시 기본 Login페이지로 이동
                                .usernameParameter("email")
                                .passwordParameter("memberPw")
                                .loginProcessingUrl("/login") // 로그인 처리 URL
//                        .defaultSuccessUrl("/post/all", true)
//                        .failureUrl("/test/testF")
                                .permitAll()  // 모든 사용자가 접근 가능
                )
                .logout(logout -> logout
                                .logoutUrl("/logout")
//                        .deleteCookies("JSESSIONID")
                                .invalidateHttpSession(true)    //사용자가 로그아웃할 때 현재 세션을 무효화하여, 해당 세션에 연결된 모든 정보를 삭제합니다.
//                        .logoutSuccessUrl("/post/all")
                                .permitAll()
                );
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .accessDeniedPage("/")
//                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();  //DelegatingPasswordEncoder 반환,각 암호화 방식은 id를 가지고 있으며, 암호화된 비밀번호는 {id}로 시작하여 어떤 방식이 사용되었는지 명시합니다
    }

}
