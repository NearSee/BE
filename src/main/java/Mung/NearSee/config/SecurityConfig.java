package Mung.NearSee.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource; // CORS 설정 주입
    @Autowired
    public SecurityConfig(@Qualifier("customCorsConfigurationSource") CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/kakao/callback").permitAll()  // 이 경로를 인증 없이 접근 가능하도록 설정
                        .anyRequest().authenticated()
                )
                .oauth2Login()
                .and()
                .csrf().disable()
                .cors(cors -> cors.configurationSource(corsConfigurationSource));
        return http.build();
    }
}

