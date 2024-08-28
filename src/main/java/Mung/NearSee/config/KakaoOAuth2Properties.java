package Mung.NearSee.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@Component
public class KakaoOAuth2Properties {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String authorizationUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;

    private static final Logger logger = LoggerFactory.getLogger(KakaoOAuth2Properties.class);
    @PostConstruct
    public void logProperties() {
        logger.info("Client ID: {}", clientId);
        logger.info("Client Secret: {}", clientSecret);
        logger.info("Redirect URI: {}", redirectUri);
        logger.info("Authorization URI: {}", authorizationUri);
        logger.info("Token URI: {}", tokenUri);
        logger.info("User Info URI: {}", userInfoUri);
    }

}


