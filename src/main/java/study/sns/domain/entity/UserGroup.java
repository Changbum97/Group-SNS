package study.sns.domain.entity;

import lombok.*;
import study.sns.domain.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "userGroup", orphanRemoval = true)
    private List<Story> stories;

    public UserGroup(User user, Group group) {
        this.user = user;
        this.group = group;
    }
}
