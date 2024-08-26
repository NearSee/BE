package Mung.NearSee.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()
                .cors() // CORS에러 방지

                // 세션사용안함
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 접근권한 설정
                .and().authorizeHttpRequests()
                .requestMatchers(HttpMethod.OPTIONS).permitAll() // CORS Preflight 방지
                .requestMatchers("/", "/h2-console/**", "/member/login/**").permitAll()
                .anyRequest().authenticated();

//                // JWT토큰관련예외처리 -> 나중에
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint()
//                .accessDeniedHandler()
//                .and()
//                .addFilterBefore(new (), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}

