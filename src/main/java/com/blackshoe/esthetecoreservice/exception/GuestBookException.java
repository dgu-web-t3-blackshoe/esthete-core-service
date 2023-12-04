package com.blackshoe.esthetecoreservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GuestBookException extends RuntimeException {
    private final GuestBookErrorResult guestBookErrorResult;
}