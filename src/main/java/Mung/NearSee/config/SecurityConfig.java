package Mung.NearSee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/kakao/login").permitAll()  // 이 경로를 인증 없이 접근 가능하도록 설정
                        .anyRequest().authenticated()
                )
                .oauth2Login();  // OAuth2 로그인 설정

        return http.build();

    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost:8000") // 프론트엔드 도메인
//                        .allowedMethods("*")
//                        .allowedHeaders("*");
//            }
//        };
//    }
}

