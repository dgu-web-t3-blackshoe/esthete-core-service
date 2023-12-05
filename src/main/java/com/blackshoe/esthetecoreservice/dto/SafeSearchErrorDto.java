package com.blackshoe.esthetecoreservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SafeSearchErrorDto {
    private String  error;
    private SafeSearchData data;

    @Data
    @Builder
    public static class SafeSearchData {
        private String  adult;
        private String  spoof;
        private String  medical;
        private String  violence;
        private String  racy;
    }
}
