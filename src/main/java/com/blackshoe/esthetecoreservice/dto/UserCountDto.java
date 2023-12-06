package com.blackshoe.esthetecoreservice.dto;

import lombok.Data;

@Data
public class UserCountDto {
    private Long count;

    public UserCountDto(Long count) {
        this.count = count;
    }
}
