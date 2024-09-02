package com.icetea.project.MonStu.controller;

import com.icetea.project.MonStu.dto.JwtResponse;
import com.icetea.project.MonStu.dto.LoginRequest;
import com.icetea.project.MonStu.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, loginRequest.getMemberPw())
            );

            String token = jwtTokenProvider.createToken(authentication);
            log.info("{} : Token Created", email);
            return ResponseEntity.ok(new JwtResponse(token,email));
        } catch (AuthenticationException e) {
            log.info("{} : Login denied - {}", email,e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("일치하는 정보가 없습니다.");
        }
    }

}
