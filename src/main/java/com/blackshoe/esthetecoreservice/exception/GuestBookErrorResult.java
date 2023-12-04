package com.blackshoe.esthetecoreservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GuestBookErrorResult {
    GUEST_BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "방명록을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
