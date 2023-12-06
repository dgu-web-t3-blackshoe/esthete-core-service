package com.blackshoe.esthetecoreservice.dto;

import lombok.Data;

@Data
public class ExhibitionCountDto {
    private Long count;

    public ExhibitionCountDto(Long count) {
        this.count = count;
    }
}
