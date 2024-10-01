package com.icetea.project.MonStu.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/* HTTP 요청을 가로채서 JWT(JSON Web Token)를 추출하고, 이 토큰이 유효한지 확인, 토큰이 유효하다면, 인증 정보를 Spring Security 컨텍스트에 설정
   OncePerRequestFilter를 확장하며, 이는 요청이 들어올 때마다 한 번씩 실행되는 필터를 의미
*/
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //JwtTokenProvider -> JWT의 생성, 검증, 인증 정보 추출 등을 담당하는 클래스
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // JWT 토큰 추출
        String token = jwtTokenProvider.resolveToken(request);

        // validateToken -> 토큰이 유효한지 검사하고, getAuthentication -> 인증정보 가져옴, SecurityContextHolder에 설정(인증된 사용자로 설정)
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 다음 필터로 체인 전달
        chain.doFilter(request, response);
    }

}
