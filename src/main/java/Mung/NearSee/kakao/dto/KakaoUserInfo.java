package Mung.NearSee.kakao.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KakaoUserInfo {
    private String id;
    private String connectedAt;
    private KakaoAccount kakaoAccount;


    public static class KakaoAccount {

        private String email;
        private Profile profile;

        public static class Profile {

            private String nickname;
            private String profileImageUrl;

        }
    }
}
