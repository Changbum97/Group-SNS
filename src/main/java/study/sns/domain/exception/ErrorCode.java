package study.sns.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    NOT_NULL(HttpStatus.BAD_REQUEST, "공백일 수 없습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    DUPLICATED_LOGIN_ID(HttpStatus.CONFLICT, "아이디가 중복됩니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "닉네임이 중복됩니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이메일이 중복됩니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),
    INVALID_LOGIN_ID(HttpStatus.BAD_REQUEST, "아이디가 올바르지 않습니다."),
    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "닉네임이 올바르지 않습니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "이메일이 올바르지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "파기된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 토큰입니다."),
    DUPLICATED_GROUP_NAME(HttpStatus.CONFLICT, "그룹명이 중복됩니다."),
    INVALID_GROUP_NAME(HttpStatus.BAD_REQUEST, "그룹명이 올바르지 않습니다."),
    INVALID_ENTER_CODE(HttpStatus.BAD_REQUEST, "입장 코드가 올바르지 않습니다."),
    MAX_GROUP(HttpStatus.BAD_REQUEST, "더 이상 그룹에 참여할 수 없습니다."),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹이 존재하지 않습니다."),
    ALREADY_JOIN_GROUP(HttpStatus.BAD_REQUEST, "이미 참여한 그룹입니다"),
    USER_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 그룹에 속해있지 않습니다."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "날짜가 올바르지 않습니다."),
    STORY_NOT_FOUND(HttpStatus.NOT_FOUND, "스토리가 존재하지 않습니다."),
    S3_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 에러가 발생하였습니다."),
    MAX_STORY(HttpStatus.BAD_REQUEST, "해당 날짜에는 더 이상 스토리를 작성할 수 없습니다."),
    MAX_IMAGES(HttpStatus.BAD_REQUEST, "스토리에 업로드 할 수 있는 이미지의 개수를 초과했습니다."),
    MAX_USERS(HttpStatus.BAD_REQUEST, "그룹에 더 이상 유저를 추가할 수 없습니다..")
    ;

    private HttpStatus status;
    private String message;
}
