package com.blackshoe.esthetecoreservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ErrorDto {
    private String  error;
}
