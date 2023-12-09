package com.blackshoe.esthetecoreservice.exception;

import com.blackshoe.esthetecoreservice.dto.SafeSearchErrorDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CopyrightException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.ACCEPTED;

    private final String message;

    private final String originalPhotoId;

    private final String originalPhotoUserId;

    private final String originalPhotoUserNickname;

    public CopyrightException(String message, String originalPhotoId,
                              String originalPhotoUserId, String originalPhotoUserNickname) {
        this.message = message;
        this.originalPhotoId = originalPhotoId;
        this.originalPhotoUserId = originalPhotoUserId;
        this.originalPhotoUserNickname = originalPhotoUserNickname;
    }
}
