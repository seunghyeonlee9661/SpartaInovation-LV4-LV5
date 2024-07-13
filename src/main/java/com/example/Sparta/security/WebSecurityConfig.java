package com.example.Sparta.security;

import com.example.Sparta.filter.JwtAuthenticationFilter;
import com.example.Sparta.filter.JwtAuthorizationFilter;
import com.example.Sparta.filter.LoginRedirectFilter;
import com.example.Sparta.handler.AccessDeniedHandler;
import com.example.Sparta.handler.AuthenticationEntryPoint;
import com.example.Sparta.global.JwtUtil;
import com.example.Sparta.handler.AuthenticationSuccessHandler;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true)  // @Secured 애노테이션 활성화
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final LoginRedirectFilter loginRedirectFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;


    public WebSecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration, LoginRedirectFilter loginRedirectFilter, AuthenticationEntryPoint authenticationEntryPoint, AccessDeniedHandler accessDeniedHandler, AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
        this.loginRedirectFilter = loginRedirectFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());
        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 접근 가능 범위 설정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        // resources 접근 허용 설정
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // 로그인 없이 접근 가능한 경로
                        .requestMatchers(
                                "/api/user/login", // 로그인
                                "/api/user/signup", // 회원가입
                                "/teacher/*", // 강사 페이지
                                "/lecture/*", // 강의 페이지
                                "/", // 강의 및 강사 목록 페이지
                                "/error" // 오류 페이지
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/teacher", // 강사 정보 불러오기
                                "/api/lecture", // 강의 정보 불러오기
                                "/api/teachers", // 강사 정보 불러오기
                                "/api/lectures" // 강의 정보 불러오기
                        ).permitAll() 
                        // 그 외 모든 요청 인증처리
                        .anyRequest().authenticated()
        );

        // 에러 처리를 위한 핸들러 설정
        http.exceptionHandling((exceptionHandling) -> {
            exceptionHandling.accessDeniedHandler(accessDeniedHandler);
            exceptionHandling.authenticationEntryPoint(authenticationEntryPoint);
        });

        http.formLogin((formLogin) -> formLogin.
                loginPage("/login")
                .loginProcessingUrl("/api/user/login")
                .successHandler(authenticationSuccessHandler)
                .failureUrl("/login?error")
                .permitAll()
        );
        // JWT 검증 및 인가 필터
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        // 로그인 및 JWT 생성 필터
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // 로그인 유저 리다이렉트 필터
        http.addFilterBefore(loginRedirectFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /* 패스워드 인코딩 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}