package com.blackshoe.esthetecoreservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AbusingReportErrorResult {

    GUEST_BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "신고할 방명록을 찾을 수 없습니다."),
    PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND, "신고할 사진을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "신고자를 찾을 수 없습니다."),
    ABUSING_REPORT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "신고에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
