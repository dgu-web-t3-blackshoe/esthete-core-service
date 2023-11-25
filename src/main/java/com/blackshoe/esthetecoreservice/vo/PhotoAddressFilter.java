package com.blackshoe.esthetecoreservice.vo;

import com.blackshoe.esthetecoreservice.exception.PhotoErrorResult;
import com.blackshoe.esthetecoreservice.exception.PhotoException;
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

    public PhotoAddressSearchType getSearchType() {
        if (!state.equals("") && !city.equals("") && !town.equals("")) {
            return PhotoAddressSearchType.TOWN;
        }
        if (!state.equals("") && !city.equals("")) {
            return PhotoAddressSearchType.CITY;
        }
        if (!state.equals("")) {
            return PhotoAddressSearchType.STATE;
        }
        throw new PhotoException(PhotoErrorResult.INVALID_ADDRESS_FILTER);
    }
}
