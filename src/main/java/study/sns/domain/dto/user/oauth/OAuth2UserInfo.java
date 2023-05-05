package study.sns.domain.dto.user.oauth;


import java.util.Map;

public interface OAuth2UserInfo {

    Map<String, Object> getAttributes();
    String getProviderId();
    String getProvider();
    String getName();
    String getEmail();
}
