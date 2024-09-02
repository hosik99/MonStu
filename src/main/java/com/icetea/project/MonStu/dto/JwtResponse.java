package com.icetea.project.MonStu.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

/*
* 사용자가 로그인에 성공했을 때, 서버가 클라이언트에게 반환하는 JWT 토큰과 같은 인증 정보를 담는 DTO
* */
@Getter
@Setter
@ToString
public class JwtResponse {

    private String token;  // JWT 토큰

    @Value("${jwt.token.type}")
    private String type;  // 토큰 타입

    private String email;  // 사용자 이메일

    public JwtResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }
}
