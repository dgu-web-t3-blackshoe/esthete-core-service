package com.blackshoe.esthetecoreservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorResult {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    SUPPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "후원 정보를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
