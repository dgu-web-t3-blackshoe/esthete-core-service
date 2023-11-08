package com.blackshoe.esthetecoreservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {
    private String  error;
}
