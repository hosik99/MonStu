package com.icetea.project.MonStu.security;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.ArrayList;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private final long validityInMilliseconds = 60 * 60 * 1000; // 1시간

    public String createToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes());

        return builder.compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                claims.getSubject(), "", new ArrayList<>());

        return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token); //토큰 검증 (만료시간 등)
            return true;
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우
            return false;
        } catch (UnsupportedJwtException e) {
            // 지원되지 않는 JWT인 경우
            return false;
        } catch (MalformedJwtException e) {
            // 잘못된 형식의 JWT인 경우
            return false;
        } catch (SignatureException e) {
            // 서명 검증 실패인 경우
            return false;
        } catch (Exception e) {
            // 기타 예외
            return false;
        }
    }
}
