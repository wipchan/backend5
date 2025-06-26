package org.example.backendproject.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtKey {

    @Value("${jwt.secretKey}") // application.properties 값을 가져온다.
    private String secretKey;


    // 서명키를 만들어서 반환하는 메서드
    @Bean
    public SecretKey secretKey(){
        byte[] keyBytes = secretKey.getBytes(); // 설정 파일에서 불러온 키 값을 바이트로 배열로 변환
        return new SecretKeySpec(keyBytes, "HmacSHA512"); // 바이트 배열을 HmacSHA256 용 security 객체로 매핑
    }
}
