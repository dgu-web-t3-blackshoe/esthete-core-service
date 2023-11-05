package com.blackshoe.esthetecoreservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude
public class ResponseDto<T> {
    private T payload;
    private String error;

    @Builder
    private ResponseDto(T payload, String error) {
        this.payload = payload;
        this.error = error;
    }
}
