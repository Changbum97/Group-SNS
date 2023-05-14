package study.sns.domain.entity;

import lombok.*;
import study.sns.domain.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"group\"")
public class Group extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String enterCode;

    @OneToMany(mappedBy = "group")
    private List<UserGroup> userGroups;

    public void addUser(UserGroup userGroup) {
        userGroups.add(userGroup);
    }


}
