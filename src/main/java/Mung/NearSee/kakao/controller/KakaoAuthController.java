package Mung.NearSee.kakao.controller;


import Mung.NearSee.kakao.dto.KakaoUserInfo;
import Mung.NearSee.kakao.dto.OAuthSignInResponse;
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
    public ResponseEntity<OAuthSignInResponse> login(@RequestParam("code") String code) {
        try {
            OAuthSignInResponse response = kakaoAuthService.login(code);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 예외 처리
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "로그인 실패");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((OAuthSignInResponse) errorResponse);
        }
    }
}
