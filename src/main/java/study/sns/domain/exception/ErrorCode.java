package study.sns.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 접근입니다."),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "파기된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 토큰입니다.")
    ;

    private HttpStatus status;
    private String message;
}
