package study.sns.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.sns.domain.BaseEntity;
import study.sns.domain.dto.user.UserRole;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;
    private String nickname;
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    // Oauth Login
    private String provider;
    private String providerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public void setNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void setNewPassword(String password) {
        this.password = password;
    }
}
