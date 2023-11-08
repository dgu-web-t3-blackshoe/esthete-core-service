package com.blackshoe.esthetecoreservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExhibitionException extends RuntimeException {
    private final ExhibitionErrorResult exhibitionErrorResult;
}
