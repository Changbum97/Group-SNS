package study.sns.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import study.sns.domain.exception.AppException;
import study.sns.domain.exception.AppExceptionDto;

@Getter
@AllArgsConstructor
public class Response<T> {

    private String resultCode;
    private T result;

    public static Response error(AppException e) {
        return new Response<>("ERROR", AppExceptionDto.of(e));
    }

    public static <T> Response<T> success(T resultObject) {
        return new Response<>("SUCCESS", resultObject);
    }
}
