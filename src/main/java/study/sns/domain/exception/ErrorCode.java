package study.sns.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "권한이 없습니다.")
    ;

    private HttpStatus status;
    private String message;
}
