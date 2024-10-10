package Mung.NearSee.kakao.service;

import Mung.NearSee.config.KakaoOAuth2Properties;


import Mung.NearSee.kakao.dto.KakaoUserInfo;
import Mung.NearSee.kakao.dto.OAuthSignInResponse;
import Mung.NearSee.kakao.token.OAuthToken;
import Mung.NearSee.user.entity.User;
import Mung.NearSee.user.service.UserService;
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

    private final UserService userService;

    public KakaoAuthService(KakaoOAuth2Properties kakaoOAuth2Properties, WebClient.Builder webClientBuilder, UserService userService) {
        this.kakaoOAuth2Properties = kakaoOAuth2Properties;
        this.webClient = webClientBuilder.build();
        this.userService = userService;
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


    //로그인 -> 시용자 정보 DB저장
    // 사용자 정보를 DB에 저장하고 OAuthSignInResponse 생성
    public OAuthSignInResponse login(KakaoUserInfo userInfo, OAuthToken token) {
        // 사용자 정보를 DB에 저장 또는 업데이트
        User user = userService.saveOrUpdateUser(userInfo);

        // OAuthSignInResponse 객체 생성
        OAuthSignInResponse oAuthSignInResponse = OAuthSignInResponse.builder()
                .id(user.getUserId())
                .kakaoId(user.getKakaoId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .image(user.getProfileImage())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();

        return oAuthSignInResponse;
    }
}
