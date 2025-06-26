package org.example.backendproject.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backendproject.security.core.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    // JwtTokenFillter 모든 HTTP 요청을 가로채서 JWT 토큰을 검사하는 필터 역할
    // OncePerRequestFilter 한 요청당 한 번만 실행되는 필터 역할

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    // HTTP 매 요청마다 호출
    @Override
    protected void doFilterInternal(HttpServletRequest request, // Http 요청
                                    HttpServletResponse response, // Http 응답
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = getTokenFromRequest(request); // 요청 헤더에서 토큰 추출

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {

            UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(accessToken);
            // 토큰에서 사용자를 꺼내서 담은 사용자 인증 객체
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // http요청으로부터 부가 정보를 추출해서 사용자 인증 객체에 넣어줌
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            // 토큰에서 사용자 인증정보를 조회해서 인증정보를 현재 스레드에 인증된 사용자로 등록

            String url = request.getRequestURI().toString();
            String method = request.getMethod(); // GET , POST , PUT
            System.out.println("현재 들어온 HTTP 요청 => " + url);
            System.out.println("👉 추출된 AccessToken: " + accessToken);
        }

        /**
         * CharacterEncodingFilter : 문자 인코딩 처리
         * CorsFilter : CORS 정책 처리
         * CsrfFilter : CSRF 보안 처리
         * JWTTokenFilter : JWT 토큰 처리 (핵심)
         * SecurityContextFilter : 인증/인가 정보 저장
         * ExceptionFilter : 예외 처리
         */
        filterChain.doFilter(request,response); // JwtTokenFilter 를 거치고 다음 필터로 넘어감
    }

    // HTTP 요청 헤더에서 토큰을 추출하는 메서드,
    public String getTokenFromRequest(HttpServletRequest request){

        // 1. 쿠키에서 accessToken 확인
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (jakarta.servlet.http.Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
             return bearerToken.substring(7);
        }
        return null;
    }

    // http 요청에서 사용자 인증 정보를 담는 객체
    private UsernamePasswordAuthenticationToken getAuthentication(String token){

        // JWT 토큰에서 사용자 id 추출
        Long userid = jwtTokenProvider.getUserIdFromToken(token);

        // 위 추출한 id를 DB에서 사용자 정보 조회
        UserDetails userDetails = customUserDetailsService.loadUserById(userid);

        return new UsernamePasswordAuthenticationToken(
                userDetails, // 사용자 정보
                null, // 이미 인증한 사용자 null
                userDetails.getAuthorities() // 사용자의 권한
        );
    }

}
