package com.example.Sparta.security;

import com.example.Sparta.filter.JwtAuthenticationFilter;
import com.example.Sparta.filter.JwtAuthorizationFilter;
import com.example.Sparta.filter.LoginRedirectFilter;
import com.example.Sparta.global.JwtUtil;
import com.example.Sparta.global.UserInterceptor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final LoginRedirectFilter loginRedirectFilter;

    public WebSecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration,LoginRedirectFilter loginRedirectFilter) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
        this.loginRedirectFilter = loginRedirectFilter;
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
                        // '/api/user/'로 시작하는 요청 모두 접근 허가
                        .requestMatchers("/api/user/**").permitAll()
                        // 메인 페이지 : 허용
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/teacher").permitAll()
                        .requestMatchers("/lecture").permitAll()
//                      // 강사 목록 불러오는 요청 : 허용
                        .requestMatchers(HttpMethod.GET, "/api/teacher").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/teachers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/lecture").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/lectures").permitAll()
                        // 그 외 모든 요청 인증처리
                        .anyRequest().authenticated()
        );

        http.formLogin((formLogin) -> formLogin.
                loginPage("/login")
                .loginProcessingUrl("/api/user/login")
                .defaultSuccessUrl("/")
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