package com.blackshoe.esthetecoreservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorResult {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    SUPPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "후원 정보를 찾을 수 없습니다."),
    INVALID_SORT_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 정렬 타입입니다."),
    GENRE_NOT_FOUND(HttpStatus.NOT_FOUND, "장르 정보를 찾을 수 없습니다."),
    INVALID_PROFILEIMG_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 프로필 이미지 타입입니다."),
    PROFILEIMG_NOT_FOUND(HttpStatus.NOT_FOUND, "프로필 이미지를 찾을 수 없습니다."),
    PROFILEIMG_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "프로필 이미지 업로드에 실패했습니다."),
    INVALID_PROFILEIMG_SIZE(HttpStatus.BAD_REQUEST, "프로필 이미지의 크기가 너무 큽니다."),
    PROFILEIMG_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "프로필 이미지 삭제에 실패했습니다."),
    USER_SIGN_UP_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
