package com.blackshoe.esthetecoreservice.vo;

import lombok.Builder;
import lombok.Data;

@Data
public class PhotoAddressFilter {
    private String state;
    private String city;
    private String town;

    @Builder
    public PhotoAddressFilter(String state, String city, String town) {
        this.state = state;
        this.city = city;
        this.town = town;
    }
}
