package Mung.NearSee.kakao.service;

import Mung.NearSee.config.KakaoOAuth2Properties;


import Mung.NearSee.kakao.dto.KakaoUserInfo;
import Mung.NearSee.kakao.dto.OAuthSignInResponse;
import Mung.NearSee.kakao.token.OAuthToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class KakaoAuthService {

    private static final Logger logger = LoggerFactory.getLogger(KakaoAuthService.class);
    private final KakaoOAuth2Properties kakaoOAuth2Properties;
    private final WebClient webClient;

    public KakaoAuthService(KakaoOAuth2Properties kakaoOAuth2Properties, WebClient.Builder webClientBuilder) {
        this.kakaoOAuth2Properties = kakaoOAuth2Properties;
        this.webClient = webClientBuilder.build();
    }

    // 인가 코드를 받아 Kakao 인증 URL을 생성 <- 프론트엔드
    public String getKakaoAuthorizationUrl() {
        String url  = kakaoOAuth2Properties.getAuthorizationUri()
                + "?response_type=code&client_id=" + kakaoOAuth2Properties.getClientId()
                + "&redirect_uri=" + kakaoOAuth2Properties.getRedirectUri();

        logger.info("Kakao authorization URL: {}", url);
        return url;
    }

    // 인가 코드로 사용해 액세스 토큰을 요청
    public OAuthToken getToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOAuth2Properties.getClientId());
        params.add("redirect_uri", kakaoOAuth2Properties.getRedirectUri());
        params.add("code", code);
        params.add("client_secret", kakaoOAuth2Properties.getClientSecret());

        logger.info("토큰을 요청하는 중...");
        // POST 방식으로 key-value 데이터 요청
        OAuthToken token = webClient.post()
                .uri(kakaoOAuth2Properties.getTokenUri())
                .body(BodyInserters.fromFormData(params))
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(OAuthToken.class).block();

        logger.info("토큰 발급 완료! {}", token.getAccessToken());

        return token;
    }

    //액세스 토큰으로 사용자 정보 가져오기
    public KakaoUserInfo getUserInfo(String accessToken) {
        KakaoUserInfo userInfo = webClient.mutate()
                .build()
                .get()
                .uri(kakaoOAuth2Properties.getUserInfoUri())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block();

        return userInfo;
    }

    public OAuthSignInResponse login(String code) {
        OAuthToken token = getToken(code);
        KakaoUserInfo userInfo = getUserInfo(token.getAccessToken());

        Long id = userInfo.getId();
        String nickname = userInfo.getKakao_account().getProfile().getNickname();
        String email = userInfo.getKakao_account().getEmail();

        OAuthSignInResponse oAuthSignInResponse = OAuthSignInResponse.builder()
                .id(id)
                .nickname(nickname)
                .email(email)
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();

        return oAuthSignInResponse;

    }
}
