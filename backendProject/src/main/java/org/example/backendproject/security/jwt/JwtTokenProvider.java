package org.example.backendproject.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.example.backendproject.security.core.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    /** JWT 토큰 생성 및 추출 검증하는 클래스  **/

    private final SecretKey secretKey; // 토큰을 만들 때 사용하는 키

    // 현재 로그인이 완료된 사용자 정보를 기반으로 access, refresh token 발급
    public String generateToken(Authentication authentication, Long expirationMillis){

        // 현재 로그인한 사용자의 정보를 꺼냄
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Date expiryDate = new Date(new Date().getTime() + expirationMillis); // 토큰 만료시간 생성 (밀리초 단위까지)
        //payload
        Claims claims = Jwts.claims();
        claims.put("user-id", customUserDetails.getId());
        claims.put("username", customUserDetails.getUsername());

        return Jwts.builder()
                .setSubject(customUserDetails.getUsername()) // 이 JWT 토큰의 주체를 지정
                .setClaims(claims) // payload
                .setIssuedAt(new Date()) // 토큰 발급시간
                .setExpiration(expiryDate) // 토큰 만료 시간
                .signWith(secretKey, SignatureAlgorithm.HS512) // 시크릿 키와 알고리즘을 이용해 암호화해서 서명
                .compact();  // <- 에서 저장한 정보들을 최종적으로 문자열로 만들어주는 메서드
    }
    // Jwt 토큰에서 사용자 ID를 추출하는 메서드
    public Long getUserIdFromToken(String token){
        return Jwts
                .parserBuilder() //Jwt 토큰을 해석하겠다고 선언
                .setSigningKey(secretKey) // 토큰을 검증하기 위해 비밀키 사용
                .build() // 해석할 준비 완료
                .parseClaimsJws(token) // 전달을 받은 토큰을 파싱
                .getBody() //파싱한 토큰의 payload 부분을 추출
                .get("user-id", Long.class); // user-id를 반환
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }
        catch (MalformedJwtException e){
            return false;
            // 토큰 형식이 잘못 되었을 때
        }
        catch (ExpiredJwtException e){
            // 토큰이 만료가 되었을 때
            return false;
        }
        catch (UnsupportedJwtException e){
            // 지원하지 않는 토큰일 때
            return false;
        }
        catch (IllegalArgumentException e){
            // 토큰 문자열이 비어있거나 이상할 때
            return false;
        }
        catch (JwtException e){
            // 기타 예외
            return false;
        }

    }

}
