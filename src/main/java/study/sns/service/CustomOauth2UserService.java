package study.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import study.sns.domain.enum_class.UserRole;
import study.sns.domain.dto.user.oauth.GoogleUserInfo;
import study.sns.domain.dto.user.oauth.KakaoUserInfo;
import study.sns.domain.dto.user.oauth.OAuth2UserInfo;
import study.sns.domain.dto.user.oauth.UserDetail;
import study.sns.domain.entity.User;
import study.sns.domain.exception.AppException;
import study.sns.domain.exception.ErrorCode;
import study.sns.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        String provider = userRequest.getClientRegistration().getRegistrationId();

        switch (provider) {
            case "google":
                oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
                break;
            case "kakao":
                oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
                break;
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();

        // OAuth Login User은 loginId, password로 로그인하지 않지만 임의로 생성해줌
        String loginId = provider + "_" + providerId;
        String password = encoder.encode("1234");

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        User user = null;
        // OAuth Login을 처음 한 경우 => 회원가입 진행
        if (optionalUser.isEmpty()) {
            if (userRepository.existsByEmail(email)) {
                throw new AppException(ErrorCode.DUPLICATED_EMAIL);
            }

            User newUser = User.builder()
                    .loginId(loginId)
                    .password(password)
                    .email(email)
                    .nickname("")
                    .userRole(UserRole.BRONZE)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            user = userRepository.save(newUser);
        } else {
            user = optionalUser.get();
        }

        // OAuth Login이 처음이 아닌 경우이거나 방금 회원가입을 진행 시킨 경우 => 권한 부여
        return new UserDetail(user.getId(), user.getLoginId(), user.getPassword(), user.getUserRole().name(), user.getNickname());
    }
}
