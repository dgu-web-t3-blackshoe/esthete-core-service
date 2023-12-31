package com.blackshoe.esthetecoreservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserException extends RuntimeException {
    private final UserErrorResult userErrorResult;
}
