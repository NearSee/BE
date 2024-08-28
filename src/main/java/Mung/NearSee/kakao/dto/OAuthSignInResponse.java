package Mung.NearSee.kakao.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class OAuthSignInResponse {
    private Long id;
    private String nickname;
    private String email;
    private String accessToken;
    private String refreshToken;


    @Builder
    public OAuthSignInResponse(Long id, String nickname, String email, String accessToken, String refreshToken, Date refreshTokenExpiration) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}