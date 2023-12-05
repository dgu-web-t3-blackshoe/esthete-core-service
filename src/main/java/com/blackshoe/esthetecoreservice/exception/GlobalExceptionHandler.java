package com.blackshoe.esthetecoreservice.exception;

import com.blackshoe.esthetecoreservice.dto.ErrorDto;
import com.blackshoe.esthetecoreservice.dto.SafeSearchErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDto> handleDataIntegrityViolationException(DataIntegrityViolationException e) {

        log.error("DataIntegrityViolationException", e);

        final ErrorDto errorDto = ErrorDto.builder()
                .error(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(ExhibitionException.class)
    public ResponseEntity<ErrorDto> handleExhibitionException(ExhibitionException e) {

        log.error("ExhibitionException", e);

        final ErrorDto errorDto = ErrorDto.builder()
                .error(e.getExhibitionErrorResult().getMessage())
                .build();

        return ResponseEntity.status(e.getExhibitionErrorResult().getHttpStatus()).body(errorDto);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDto> handleUserException(UserException e) {

        log.error("UserException", e);

        final ErrorDto errorDto = ErrorDto.builder()
                .error(e.getUserErrorResult().getMessage())
                .build();

        return ResponseEntity.status(e.getUserErrorResult().getHttpStatus()).body(errorDto);
    }

    @ExceptionHandler(AbusingReportException.class)
    public ResponseEntity<ErrorDto> handleAbusingReportException(AbusingReportException e) {

        log.error("AbusingReportException", e);

        final ErrorDto errorDto = ErrorDto.builder()
                .error(e.getAbusingReportErrorResult().getMessage())
                .build();

        return ResponseEntity.status(e.getAbusingReportErrorResult().getHttpStatus()).body(errorDto);
    }

    @ExceptionHandler(PhotoException.class)
    public ResponseEntity<ErrorDto> handlePhotoException(PhotoException e) {

        log.error("PhotoException", e);

        final ErrorDto errorDto = ErrorDto.builder()
                .error(e.getPhotoErrorResult().getMessage())
                .build();

        return ResponseEntity.status(e.getPhotoErrorResult().getHttpStatus()).body(errorDto);
    }

    @ExceptionHandler(GuestBookException.class)
    public ResponseEntity<ErrorDto> handleGuestBookException(GuestBookException e) {

        log.error("GuestBookException", e);

        final ErrorDto errorDto = ErrorDto.builder()
                .error(e.getGuestBookErrorResult().getMessage())
                .build();

        return ResponseEntity.status(e.getGuestBookErrorResult().getHttpStatus()).body(errorDto);
    }

    @ExceptionHandler(SafeSearchException.class)
    public ResponseEntity<SafeSearchErrorDto> handleAbusedPhotoException(SafeSearchException e) {

        log.error("SafeSearchException", e);

        final SafeSearchErrorDto safeSearchErrorDto = SafeSearchErrorDto.builder()
                .error(e.getMessage())
                .data(e.getSafeSearchData())
                .build();

        return ResponseEntity.status(e.getHttpStatus()).body(safeSearchErrorDto);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> handleRuntimeException(RuntimeException e) {

        log.error("RuntimeException", e);

        final ErrorDto errorDto = ErrorDto.builder()
                .error(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}
