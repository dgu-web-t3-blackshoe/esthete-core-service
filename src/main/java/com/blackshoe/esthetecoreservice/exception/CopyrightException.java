package com.blackshoe.esthetecoreservice.exception;

import com.blackshoe.esthetecoreservice.dto.SafeSearchErrorDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CopyrightException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.ACCEPTED;

    private final String message;

    private final String originalPhotoId;

    public CopyrightException(String message, String originalPhotoId) {
        this.message = message;
        this.originalPhotoId = originalPhotoId;
    }
}
