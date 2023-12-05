package com.blackshoe.esthetecoreservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CopyrightErrorDto {
    private String  error;
    private String originalPhotoId;
}
