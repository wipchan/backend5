package org.example.backendproject.security.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backendproject.oauth2.OAuth2LoginSuccessHandler;
import org.example.backendproject.oauth2.OAuth2LogoutSuccessHandler;
import org.example.backendproject.oauth2.OAuth2UserService;
import org.example.backendproject.oauth2.RedisOAuth2AuthorizationRequestRepository;
import org.example.backendproject.security.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    private final OAuth2LogoutSuccessHandler oAuth2LogoutSuccessHandler;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2UserService oAuth2UserService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new RedisOAuth2AuthorizationRequestRepository(redisTemplate);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                // 사용자가 모르게 악성 요청 방지
                // JWt는 매 요청마다 헤더를 보냄
                // CSRF 보호 기능 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests((auth) -> auth
                                //인증 필요없음
                                .requestMatchers("/","/index.html", "/*.html", "/favicon.ico",
                                        "/css/**", "/fetchWithAuth.js","/js/**", "/images/**",
                                        "/.well-known/**").permitAll() // 정적 리소스 누구나 접근
                                .requestMatchers("/boards/**",  "/boards","/api/comments/**").authenticated()

                                //인증필요
                                .requestMatchers(
                                        "/api/auth/**",       // 로그인/회원가입/로그아웃 등 인증 없이 사용
//                        "/api/comments/**",   // 댓글 읽기 등 인증 없이 사용
                                        "/oauth2/**",         // 소셜 로그인 엔드포인트는 누구나 접근
                                        "/login/**",          // 스프링 시큐리티 내부 로그인 관련 엔드포인트
                                        "/ws-gpt", "/ws-chat", // 웹소켓 핸드셰이크
                                        "/actuator/prometheus" //프로메테우스
                                ).permitAll() // 웹소켓 핸드셰이크는 모두 허용!

                                .requestMatchers(
                                        "/api/user/**",
                                        //  "/boards/**",
                                        "/api/rooms/**"
                                ).authenticated() //인증이 필요한 경로
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unathorized");
                        })
                        .authenticationEntryPoint((request, response, authExcetption) -> {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                        })

                )
                //스프링 시큐리티에서 세션관리정책을 설정하는 부분
                //기본적으로 스프링시큐리티는 세션을 생성함
                //하지만 JWT 기반 인증은 세션상태를 저장하지 않는 무상태방식
                //인증 정보를 세션에 저장하지 않고, 매 요청마다 토큰으로 인증

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 매 요청마다 적용한 필터
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)

                .oauth2Login(oauth2->oauth2
                        .loginPage("/index.html")
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)

                        .authorizationEndpoint(authorization -> authorization
                                .authorizationRequestRepository(authorizationRequestRepository()))
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(oAuth2LogoutSuccessHandler)
                        .permitAll()
                )
                .build();
    }


    // 회원 가입시에 비밀번호를 암호화해주는 메서드
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

