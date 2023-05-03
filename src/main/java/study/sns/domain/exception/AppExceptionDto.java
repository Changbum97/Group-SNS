package study.sns.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppExceptionDto {

    private String errorCode;
    private String message;

    public static AppExceptionDto of(AppException e) {
        return new AppExceptionDto(e.getErrorCode().name(), e.getMessage());
    }
}
