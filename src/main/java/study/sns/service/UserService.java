package study.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import study.sns.domain.dto.user.*;
import study.sns.domain.entity.User;
import study.sns.domain.exception.AppException;
import study.sns.domain.exception.ErrorCode;
import study.sns.jwt.JwtTokenUtil;
import study.sns.repository.UserRepository;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final StringRedisTemplate stringRedisTemplate;

    @Value("${jwt.token.secret}")
    private String secretKey;
    @Value("${jwt.duration.access-token}")
    private Long accessTokenDurationSec;
    @Value("${jwt.duration.refresh-token}")
    private Long refreshTokenDurationSec;

    public UserDto saveUser(UserJoinRequest req) {
        if (!checkLoginId(req.getLoginId()) || !checkNickname(req.getNickname())) {
            throw new AppException(ErrorCode.BAD_REQUEST);
        }

        User savedUser = userRepository.save( req.toEntity(encoder.encode(req.getPassword()), UserRole.USER) );
        return UserDto.of(savedUser);
    }

    public UserLoginResponse login(UserLoginRequest req) {
        User user = findByLoginId(req.getLoginId());

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        // JWT Token 발급
        String accessToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, accessTokenDurationSec * 1000);
        String refreshToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, refreshTokenDurationSec * 1000);

        // Redis에 Refresh Token 저장
        stringRedisTemplate.delete(user.getLoginId() + "_refreshToken");
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.set(user.getLoginId() + "_refreshToken", refreshToken,
                refreshTokenDurationSec, TimeUnit.SECONDS);

        return UserLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(user.getNickname())
                .build();
    }

    public Boolean checkLoginId(String loginId) {
        return !userRepository.existsByLoginId(loginId);
    }

    public Boolean checkNickname(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
