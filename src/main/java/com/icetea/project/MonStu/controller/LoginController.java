package com.icetea.project.MonStu.controller;

import com.icetea.project.MonStu.dto.JwtResponse;
import com.icetea.project.MonStu.dto.LoginRequest;
import com.icetea.project.MonStu.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;  //Spring Security 설정에서 인증 프로세스를 처리하는 데 사용
    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, loginRequest.getMemberPw())
            );

            String token = jwtTokenProvider.createToken(authentication);
                        log.info("{} : Token Created", email);

            // JWT를 HttpOnly 쿠키로 설정
            ResponseCookie cookie = ResponseCookie.from("auth-token", token)
                    .httpOnly(false)    //JavaScript를 통해 접근할 수 없게 만드는 옵션
                    .secure(false) // HTTPS에서만 전송
                    .path("/") // 모든 경로에서 접근 가능
                    .maxAge(60 * 60) // 1시간 유효
                    .sameSite("Lax") // 동일 사이트 요청에서만 전송되도록 제한
                    .domain("localhost")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body("로그인 성공");
        } catch (AuthenticationException e) {
                        log.info("{} : Login denied - {}", email,e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("일치하는 정보가 없습니다.");
        }
    }

}
