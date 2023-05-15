package study.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.sns.domain.entity.Group;
import study.sns.domain.entity.User;
import study.sns.domain.entity.UserGroup;
import study.sns.domain.exception.AppException;
import study.sns.domain.exception.ErrorCode;
import study.sns.repository.UserGroupRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final S3UploadService s3UploadService;

    public UserGroup save(User user, Group group) {
        return userGroupRepository.save( new UserGroup(user, group) );
    }

    public UserGroup findByUserAndGroup(User user, Group group) {
        return userGroupRepository.findByUserAndGroup(user, group)
                .orElseThrow(() -> new AppException(ErrorCode.USER_GROUP_NOT_FOUND));
    }

    @Transactional
    public void deleteUserGroup(UserGroup userGroup) {
        s3UploadService.deleteAllByUserGroup(userGroup);
        userGroupRepository.delete(userGroup);
    }

    public List<UserGroup> findByGroup(Group group) {
        return userGroupRepository.findByGroup(group);
    }

}
