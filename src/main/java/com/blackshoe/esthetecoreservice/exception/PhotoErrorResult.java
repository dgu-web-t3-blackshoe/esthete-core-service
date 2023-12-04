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
    PHOTO_GENRE_NOT_FOUND(HttpStatus.NOT_FOUND, "사진 장르를 찾을 수 없습니다."),
    INVALID_HASH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "해시 알고리즘이 유효하지 않습니다."),
    PHOTO_HASH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "사진 해시 생성에 실패했습니다."),
    PHOTO_ALREADY_EXISTS(HttpStatus.NOT_ACCEPTABLE, "이미 등록된 사진은 재등록할 수 없습니다."),
    INVALID_LOCATION_GROUP_TYPE(HttpStatus.BAD_REQUEST, "위치 그룹 타입이 유효하지 않습니다."),
    INVALID_SORT_TYPE(HttpStatus.BAD_REQUEST, "정렬 타입이 유효하지 않습니다."),
    INVALID_ADDRESS_FILTER(HttpStatus.BAD_REQUEST, "주소 필터가 유효하지 않습니다."),
    PHOTO_SAFE_SEARCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "사진 선정성 필터에 실패했습니다."),
    UNSAFE_PHOTO(HttpStatus.NOT_ACCEPTABLE, "선정적인 사진은 등록할 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}