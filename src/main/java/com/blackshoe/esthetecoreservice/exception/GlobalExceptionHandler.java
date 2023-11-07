package com.blackshoe.esthetecoreservice.exception;

import com.blackshoe.esthetecoreservice.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorDto> handleBindException(BindException e) {

        final String errors = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.error("BindException", errors);

        final ErrorDto errorDto = ErrorDto.builder()
                .error(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(ExhibitionException.class)
    public ResponseEntity<ErrorDto> handleExhibitionException(ExhibitionException e) {

        log.error("ExhibitionException", e);

        final ErrorDto errorDto = ErrorDto.builder()
                .error(e.getMessage())
                .build();

        return ResponseEntity.status(e.getExhibitionErrorResult().getHttpStatus()).body(errorDto);
    }
}