package study.sns.domain.entity;

import lombok.*;
import study.sns.domain.BaseEntity;
import study.sns.domain.enum_class.UserRole;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "user")
    private List<UserGroup> userGroups;

    public void setNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void setNewPassword(String password) {
        this.password = password;
    }

    public void addGroup(UserGroup userGroup) {
        userGroups.add(userGroup);
    }
}
