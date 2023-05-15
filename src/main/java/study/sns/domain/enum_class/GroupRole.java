package study.sns.domain.enum_class;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupRole {

    BRONZE("BRONZE", 1, 5, 5),
    SILVER("SILVER", 3, 5, 10),
    GOLD("GOLD", 3, 10, 20),
    DIAMOND("DIAMOND", 10, 20, 50);

    private String name;
    private Integer maxOnedayStory; // 하루에 작성할 수 있는 스토리 수
    private Integer maxImage;       // 하나의 스토리에 추가할 수 있는 이미지 수
    private Integer maxGroupUsers;  // 그룹에 참여할 수 있는 유저의 수
}
