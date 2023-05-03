package study.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import study.sns.domain.dto.user.UserDto;
import study.sns.domain.dto.user.UserJoinRequest;
import study.sns.domain.dto.user.UserLoginRequest;
import study.sns.domain.dto.user.UserRole;
import study.sns.domain.entity.User;
import study.sns.domain.exception.AppException;
import study.sns.domain.exception.ErrorCode;
import study.sns.jwt.JwtTokenUtil;
import study.sns.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    private static final long expireTimeMs = 1000 * 60 * 60;    // 1시간

    @Value("${jwt.token.secret}")
    private String secretKey;

    public UserDto saveUser(UserJoinRequest req) {
        User savedUser = userRepository.save( req.toEntity(encoder.encode(req.getPassword()), UserRole.USER) );
        return UserDto.of(savedUser);
    }

    public String login(UserLoginRequest req) {
        User user = userRepository.findByLoginId(req.getLoginId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        // JWT Token 발급
        String jwtToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, expireTimeMs);
        return jwtToken;
    }
}
