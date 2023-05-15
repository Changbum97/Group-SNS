package study.sns.domain.dto.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.sns.domain.entity.Group;
import study.sns.domain.enum_class.GroupRole;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequest {

    private String name;
    private String enterCode;

    public Group toEntity() {
        return Group.builder()
                .name(name)
                .enterCode(enterCode)
                .userGroups(new ArrayList<>())
                .groupRole(GroupRole.BRONZE)
                .build();
    }
}
