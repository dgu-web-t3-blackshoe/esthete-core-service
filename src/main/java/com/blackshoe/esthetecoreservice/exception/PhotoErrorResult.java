package com.blackshoe.esthetecoreservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PhotoErrorResult {
    EMPTY_PHOTO(HttpStatus.BAD_REQUEST, "사진 파일이 비어있습니다."),
    PHOTO_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "사진 업로드에 실패했습니다."),
    INVALID_PHOTO_SIZE(HttpStatus.BAD_REQUEST, "사진 파일의 크기가 유효하지 않습니다."),
    INVALID_PHOTO_TYPE(HttpStatus.BAD_REQUEST, "사진 파일의 확장자가 유효하지 않습니다."),
    PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND, "사진을 찾을 수 없습니다."),
    GENRE_NOT_FOUND(HttpStatus.NOT_FOUND, "장르를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}