package study.sns.domain.entity;

import lombok.*;
import study.sns.domain.BaseEntity;
import study.sns.domain.enum_class.GroupRole;

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

    @OneToMany(mappedBy = "group", orphanRemoval = true)
    private List<UserGroup> userGroups;

    @Enumerated(EnumType.STRING)
    private GroupRole groupRole;

    public void addUser(UserGroup userGroup) {
        userGroups.add(userGroup);
    }

    public void edit(String newName, String newEnterCode) {
        this.name = newName;
        this.enterCode = newEnterCode;
    }
}
