package Mung.NearSee.kakao.controller;


import Mung.NearSee.kakao.dto.KakaoUserInfo;
import Mung.NearSee.kakao.service.KakaoAuthService;

import Mung.NearSee.kakao.token.OAuthToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/kakao/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam("code") String code) {
        try {
            // 인가 코드로 액세스 토큰과 사용자 정보를 가져옵니다
            OAuthToken token = kakaoAuthService.getToken(code);
            KakaoUserInfo userInfo = kakaoAuthService.getUserInfo(token.getAccessToken());

            // 반환할 데이터를 Map에 담아서 응답
            Map<String, Object> response = new HashMap<>();
            response.put("message", "로그인 완료");
            response.put("accessToken", token.getAccessToken());
            response.put("userInfo", userInfo.getKakaoAccount());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 예외 처리
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "로그인 실패");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
