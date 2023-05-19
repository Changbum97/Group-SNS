package study.sns;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import study.sns.domain.entity.Group;
import study.sns.domain.entity.UserGroup;
import study.sns.domain.enum_class.GroupRole;
import study.sns.domain.enum_class.UserRole;
import study.sns.domain.entity.User;
import study.sns.repository.GroupRepository;
import study.sns.repository.UserGroupRepository;
import study.sns.repository.UserRepository;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class MakeInitData {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final BCryptPasswordEncoder encoder;

    //@PostConstruct
    public void makeUserAndGroupAndJoin() {

        User admin = userRepository.save(User.builder().loginId("admin").password(encoder.encode("1234")).nickname("관리자").userRole(UserRole.ADMIN).build());
        User user = userRepository.save(User.builder().loginId("user").password(encoder.encode("1234")).nickname("지나가는행인").userRole(UserRole.GOLD).build());
        User friend1 = userRepository.save(User.builder().loginId("friend1").password(encoder.encode("1234")).nickname("친구1").userRole(UserRole.BRONZE).build());
        User friend2 = userRepository.save(User.builder().loginId("friend2").password(encoder.encode("1234")).nickname("친구2").userRole(UserRole.BRONZE).build());
        User mom = userRepository.save(User.builder().loginId("mom").password(encoder.encode("1234")).nickname("엄마").userRole(UserRole.BRONZE).build());
        User dad = userRepository.save(User.builder().loginId("dad").password(encoder.encode("1234")).nickname("아빠").userRole(UserRole.BRONZE).build());
        User girlFriend = userRepository.save(User.builder().loginId("girlFriend").password(encoder.encode("1234")).nickname("여자친구").userRole(UserRole.BRONZE).build());

        Group friends = groupRepository.save(Group.builder().name("친구들").enterCode("12345").groupRole(GroupRole.SILVER).build());
        Group family = groupRepository.save(Group.builder().name("가족").enterCode("12345").groupRole(GroupRole.BRONZE).build());
        Group couple = groupRepository.save(Group.builder().name("커플").enterCode("12345").groupRole(GroupRole.BRONZE).build());

        userGroupRepository.save(UserGroup.builder().user(user).group(friends).build());
        userGroupRepository.save(UserGroup.builder().user(user).group(family).build());
        userGroupRepository.save(UserGroup.builder().user(user).group(couple).build());

        userGroupRepository.save(UserGroup.builder().user(friend1).group(friends).build());
        userGroupRepository.save(UserGroup.builder().user(friend2).group(friends).build());

        userGroupRepository.save(UserGroup.builder().user(mom).group(family).build());
        userGroupRepository.save(UserGroup.builder().user(dad).group(family).build());

        userGroupRepository.save(UserGroup.builder().user(girlFriend).group(couple).build());
    }
}
