package com.blackshoe.esthetecoreservice.exception;

import com.blackshoe.esthetecoreservice.dto.SafeSearchErrorDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class SafeSearchException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.ACCEPTED;

    private final String message;

    private final SafeSearchErrorDto.SafeSearchData safeSearchData;

    public SafeSearchException(String message, SafeSearchErrorDto.SafeSearchData safeSearchData) {
        this.message = message;
        this.safeSearchData = safeSearchData;
    }
}
