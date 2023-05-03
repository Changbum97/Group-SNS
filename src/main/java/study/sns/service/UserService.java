package study.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import study.sns.domain.dto.user.UserDto;
import study.sns.domain.dto.user.UserJoinRequest;
import study.sns.domain.dto.user.UserRole;
import study.sns.domain.entity.User;
import study.sns.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserDto saveUser(UserJoinRequest req) {
        User savedUser = userRepository.save( req.toEntity(encoder.encode(req.getPassword()), UserRole.USER) );
        return UserDto.of(savedUser);
    }
}
