package study.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.ApiIgnore;
import study.sns.domain.Response;
import study.sns.domain.dto.group.GroupDetailResponse;
import study.sns.domain.dto.group.GroupRequest;
import study.sns.domain.dto.group.GroupDto;
import study.sns.domain.entity.Group;
import study.sns.domain.entity.User;
import study.sns.domain.entity.UserGroup;
import study.sns.domain.exception.AppException;
import study.sns.domain.exception.ErrorCode;
import study.sns.repository.GroupRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;
    private final S3UploadService s3UploadService;
    private final UserGroupService userGroupService;


    @Transactional
    public GroupDto addGroup(GroupRequest req, String loginId) {

        User loginUser = null;

        try {
            loginUser = userService.findByLoginId(loginId);
            groupRequestCheck(req);
        } catch (AppException e) {
            throw e;
        }

        if (loginUser.getUserGroups().size() >= loginUser.getUserRole().getMaxGroupJoin()) {
            throw new AppException(ErrorCode.MAX_GROUP);
        }

        Group savedGroup = groupRepository.save( req.toEntity() );
        UserGroup savedUserGroup = userGroupService.save( loginUser, savedGroup );
        savedGroup.addUser(savedUserGroup);
        return GroupDto.of(savedGroup);
    }

    public GroupDto joinGroup(GroupRequest req, String loginId) {

        User loginUser = userService.findByLoginId(loginId);
        Group group = findByName(req.getName());

        // 로그인 한 유저가 가입할 수 있는 최대 그룹 수 체크
        if (loginUser.getUserGroups().size() >= loginUser.getUserRole().getMaxGroupJoin()) {
            throw new AppException(ErrorCode.MAX_GROUP);
        }
        // 그룹에서 유저를 더 받을 수 있는지 체크
        if (group.getUserGroups().size() >= group.getGroupRole().getMaxGroupUsers()) {
            throw new AppException(ErrorCode.MAX_USERS);
        }

        for (UserGroup userGroup : loginUser.getUserGroups()) {
            if (userGroup.getGroup().equals(group)) {
                throw new AppException(ErrorCode.ALREADY_JOIN_GROUP);
            }
        }

        if (!req.getEnterCode().equals(group.getEnterCode())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD, "입장 코드가 일치하지 않습니다.");
        }

        UserGroup savedUserGroup = userGroupService.save(loginUser, group);
        group.addUser(savedUserGroup);
        return GroupDto.of(group);
    }

    public List<GroupDto> getGroupList(String loginId ) {
        User user = userService.findByLoginId(loginId);
        List<GroupDto> groupDtos = new ArrayList<>();

        for (UserGroup userGroup : user.getUserGroups()) {
            groupDtos.add(GroupDto.of(userGroup.getGroup()));
        }
        return groupDtos;
    }

    public GroupDetailResponse getGroupDetail(Long groupId, String loginId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND));

        // 로그인한 유저가 그룹에 있는지 체크
        boolean check = false;
        for (UserGroup userGroup : group.getUserGroups()) {
            if (userGroup.getUser().getLoginId().equals(loginId)) {
                check = true;
            }
        }
        // 로그인한 유저가 그룹원이 아니라면 에러 발생
        if (check == false) {
            throw new AppException(ErrorCode.USER_GROUP_NOT_FOUND);
        }

        return GroupDetailResponse.of(group);
    }

    @Transactional
    public void deleteGroup(Long groupId, String loginId) {
        User loginUser = userService.findByLoginId(loginId);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND));
        userGroupService.findByUserAndGroup(loginUser, group);

        s3UploadService.deleteAllByGroup(group);
        groupRepository.delete(group);
    }

    @Transactional
    public GroupDto editGroup(Long groupId, GroupRequest req, String loginId) {
        Group group = null;

        try {
            User loginUser = userService.findByLoginId(loginId);
            group = findById(groupId);
            groupRequestCheck(req);
            userGroupService.findByUserAndGroup(loginUser, group);
        } catch (AppException e) {
            if (!e.getErrorCode().equals(ErrorCode.DUPLICATED_GROUP_NAME) || !req.getName().equals(group.getName())) {
                throw e;
            }
        }

        group.edit(req.getName(), req.getEnterCode());
        return GroupDto.of(group);
    }

    @Transactional
    public void deleteGroupUser(Long groupId, String loginId) {
        User loginUser = userService.findByLoginId(loginId);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND));
        UserGroup userGroup = userGroupService.findByUserAndGroup(loginUser, group);

        userGroupService.deleteUserGroup(userGroup);
    }

    public Boolean checkName(String name) {
        return !groupRepository.existsByName(name);
    }

    private void groupRequestCheck(GroupRequest req) {
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

    public Group findByName(String name) {
        return groupRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND));
    }

    public Group findById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND));
    }
}
