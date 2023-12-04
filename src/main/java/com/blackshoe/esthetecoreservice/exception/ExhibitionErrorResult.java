package com.blackshoe.esthetecoreservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExhibitionErrorResult {
    EXHIBITION_NOT_FOUND(HttpStatus.NOT_FOUND, "전시회를 찾을 수 없습니다."),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "전시실을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    ROOM_PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND, "전시실 사진을 찾을 수 없습니다."),
    FAILED_TO_GENERATE_PDF(HttpStatus.INTERNAL_SERVER_ERROR, "PDF 파일을 생성하는데 실패했습니다."),
    GENRE_NOT_FOUND(HttpStatus.NOT_FOUND, "장르를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
