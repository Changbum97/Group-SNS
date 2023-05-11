package study.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.sns.domain.dto.group.GroupAddRequest;
import study.sns.domain.dto.group.GroupDto;
import study.sns.domain.entity.Group;
import study.sns.domain.entity.User;
import study.sns.domain.entity.UserGroup;
import study.sns.domain.exception.AppException;
import study.sns.domain.exception.ErrorCode;
import study.sns.repository.GroupRepository;
import study.sns.repository.UserGroupRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserService userService;

    @Transactional
    public GroupDto addGroup(GroupAddRequest req, String loginId) {

        User loginUser = null;

        try {
            loginUser = userService.findByLoginId(loginId);
            groupAddRequestCheck(req);
        } catch (AppException e) {
            throw e;
        }

        Group savedGroup = groupRepository.save( req.toEntity() );
        UserGroup savedUserGroup = userGroupRepository.save( new UserGroup(loginUser, savedGroup) );
        savedGroup.addUser(savedUserGroup);
        return GroupDto.of(savedGroup);
    }

    public List<GroupDto> getGroupList(String loginId ) {
        User user = userService.findByLoginId(loginId);

        List<UserGroup> userGroups = user.getUserGroups();
        List<GroupDto> groupDtos = new ArrayList<>();

        for (UserGroup userGroup : userGroups) {
            groupDtos.add(GroupDto.of(userGroup.getGroup()));
        }
        return groupDtos;
    }

    public Boolean checkName(String name) {
        return !groupRepository.existsByName(name);
    }

    private void groupAddRequestCheck(GroupAddRequest req) {
        if (req.getName() == null || req.getEnterCode() == null) {
            throw new AppException(ErrorCode.NOT_NULL);
        } else if (!checkName(req.getName())) {
            throw new AppException(ErrorCode.DUPLICATED_GROUP_NAME);
        } else if (req.getName().length() < 2 || req.getName().length() > 15 || req.getName().contains(" ")) {
            throw new AppException(ErrorCode.INVALID_GROUP_NAME);
        } else if (req.getEnterCode().length() < 5 || req.getEnterCode().length() > 20 ||
                    req.getEnterCode().contains(" ") || req.getEnterCode().matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
            throw new AppException(ErrorCode.INVALID_ENTER_CODE);
        }
    }
}
