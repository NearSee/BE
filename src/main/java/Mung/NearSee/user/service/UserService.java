package Mung.NearSee.user.service;

import Mung.NearSee.kakao.dto.KakaoUserInfo;
import Mung.NearSee.user.entity.Role;
import Mung.NearSee.user.entity.User;
import Mung.NearSee.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 카카오 ID를 기준으로 유저를 찾거나 없으면 새로 생성
    public User saveOrUpdateUser(KakaoUserInfo kakaoUserInfo) {
        Optional<User> existUser = userRepository.findByKakaoId(kakaoUserInfo.getId());

        User user;
        if (existUser.isPresent()) {
            user = existUser.get();
        } else {
            user = new User();
            user.setKakaoId(kakaoUserInfo.getId());
        }

        user.setNickname(kakaoUserInfo.getKakao_account().getProfile().getNickname());
        user.setProfileImage(kakaoUserInfo.getKakao_account().getProfile().getProfile_image_url());
        user.setEmail(kakaoUserInfo.getKakao_account().getEmail());

        // 기본값으로 Student <- 이거 0/1로 할까..?
        if (user.getRole() == null) {
            user.setRole(Role.STUDENT);
        }
        return userRepository.save(user);
    }
}
