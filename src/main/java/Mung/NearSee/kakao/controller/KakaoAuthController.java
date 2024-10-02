package Mung.NearSee.kakao.controller;
import Mung.NearSee.kakao.dto.KakaoUserInfo;
import Mung.NearSee.kakao.dto.OAuthSignInResponse;
import Mung.NearSee.kakao.service.KakaoAuthService;
import Mung.NearSee.kakao.token.OAuthToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/kakao")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) {
        // 인가코드 받아오기
        System.out.println("Received code: " + code);

        try {
            // 1. 인가 코드로 액세스 토큰 요청
            OAuthToken token = kakaoAuthService.getToken(code);
            System.out.println("Access Token: " + token.getAccessToken());

            // 2. 액세스 토큰으로 사용자 정보 요청
            KakaoUserInfo userInfo = kakaoAuthService.getUserInfo(token.getAccessToken());
//            System.out.println("User Info: " + userInfo);

            // 3. 사용자 정보를 DB에 저장하고 응답 생성
            OAuthSignInResponse oAuthSignInResponse = kakaoAuthService.login(userInfo, token);

            // 클라이언트에게 액세스 토큰 및 사용자 정보 응답
            return ResponseEntity.ok()
                    .body(Map.of(
                            "user_info", oAuthSignInResponse // 사용자 정보 포함
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed kakaoCallback");
        }
    }
}
