package study.sns.domain.enum_class;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    BRONZE("BRONZE", 3),
    SILVER("SILVER", 5),
    GOLD("GOLD", 10),
    DIAMOND("DIAMOND", Integer.MAX_VALUE),
    ADMIN("ADMIN", Integer.MAX_VALUE);

    private String name;
    private Integer maxGroupJoin;   // 참여할 수 있는 그룹 최대 수
}
