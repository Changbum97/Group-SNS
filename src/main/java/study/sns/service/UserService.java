package study.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import study.sns.domain.dto.user.*;
import study.sns.domain.entity.User;
import study.sns.domain.enum_class.UserRole;
import study.sns.domain.exception.AppException;
import study.sns.domain.exception.ErrorCode;
import study.sns.util.JwtTokenUtil;
import study.sns.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.Duration;

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
        try {
            userJoinRequestCheck(req);
        } catch (AppException e) {
            throw e;
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
                Duration.ofSeconds(refreshTokenDurationSec));

        return UserLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(user.getNickname())
                .build();
    }

    public void logout(String loginId) {
        stringRedisTemplate.delete(loginId + "_refreshToken");
    }

    @Transactional
    public String setNickname(String nickname, String accessToken) {
        if (!checkNickname(nickname)) {
            throw new AppException(ErrorCode.DUPLICATED_NICKNAME);
        }

        String loginId = JwtTokenUtil.getLoginId(accessToken, secretKey);
        User user = findByLoginId(loginId);
        user.setNickname(nickname);
        return nickname;
    }

    public String reissueAccessToken(String refreshToken) {
        if (!refreshToken.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        String token = refreshToken.split(" ")[1];
        if (JwtTokenUtil.isExpired(token, secretKey)) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        String loginId = JwtTokenUtil.getLoginId(token, secretKey);
        User loginUser = findByLoginId(loginId);

        // Refresh Token이 Redis에 있는지 검증
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        if (stringStringValueOperations.get(loginId + "_refreshToken") == null ||
                !stringStringValueOperations.get(loginId + "_refreshToken").equals(token)) {

            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        // Refresh Token이 유효하면 Access Token을 다시 생성
        return JwtTokenUtil.createToken(loginId, secretKey, accessTokenDurationSec * 1000);
    }

    public Boolean checkLoginId(String loginId) {
        return !userRepository.existsByLoginId(loginId);
    }

    public Boolean checkNickname(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    public Boolean checkEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public User findByAccessToken(String accessToken) {
        String loginId = JwtTokenUtil.getLoginId(accessToken, secretKey);
        return findByLoginId(loginId);
    }

    private void userJoinRequestCheck(UserJoinRequest req) {
        if (req.getLoginId() == null || req.getNickname() == null || req.getPassword() == null || req.getEmail() == null) {
            throw new AppException(ErrorCode.NOT_NULL);
        } else if (!checkLoginId(req.getLoginId())) {
            throw new AppException(ErrorCode.DUPLICATED_LOGIN_ID);
        } else if (!checkNickname(req.getNickname())) {
            throw new AppException(ErrorCode.DUPLICATED_NICKNAME);
        } else if (!checkEmail(req.getEmail())) {
            throw new AppException(ErrorCode.DUPLICATED_EMAIL);
        } else if (req.getLoginId().matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*") || req.getLoginId().matches("[^a-zA-Z0-9]") ||
                req.getLoginId().length() < 3 || req.getLoginId().length() > 10) {
            throw new AppException(ErrorCode.INVALID_LOGIN_ID);
        } else if (req.getNickname().contains(" ") || req.getNickname().length() < 3 || req.getNickname().length() > 10) {
            throw new AppException(ErrorCode.INVALID_NICKNAME);
        } else if (!req.getEmail().contains("@") || !req.getEmail().contains(".") || req.getEmail().contains(" ") ||
                req.getEmail().matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
            throw new AppException(ErrorCode.INVALID_EMAIL);
        }
    }
}
