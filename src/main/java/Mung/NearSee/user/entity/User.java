package Mung.NearSee.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private Long kakaoId;

    private String nickname;
    private String profileImage;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;  // 생성 시간
    private LocalDateTime updatedAt;  // 업데이트 시간

    // 엔티티 생성 시 자동으로 생성, 업데이트 시간 설정
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}


