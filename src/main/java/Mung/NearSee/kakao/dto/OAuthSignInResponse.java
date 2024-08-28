package Mung.NearSee.kakao.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class OAuthSignInResponse {
    private Long id;
    private String nickname;
    private String email;
    private String image;
    private String accessToken;
    private String refreshToken;


    @Builder
    public OAuthSignInResponse(Long id, String nickname, String email, String image, String accessToken, String refreshToken) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.image = image;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}