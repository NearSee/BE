package Mung.NearSee.kakao.dto;


import lombok.Getter;


@Getter
public class KakaoUserInfo {
    private Long id;
    private String connected_at;
    private KakaoAccount kakao_account;


    @Getter
    public static class KakaoAccount {

        private Profile profile;
        private Boolean has_email;
        private String email;

        @Getter
        public static class Profile {

            private String nickname;
            private String thumbnail_image_url;
            private String profile_image_url;

        }
    }
}
