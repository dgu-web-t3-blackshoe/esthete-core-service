package com.blackshoe.esthetecoreservice.vo;

import com.blackshoe.esthetecoreservice.exception.PhotoErrorResult;
import com.blackshoe.esthetecoreservice.exception.PhotoException;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum LocationGroupType {
    STATE("state"),
    CITY("city"),
    TOWN("town");

    private final String locationGroupType;

    public static LocationGroupType convertParamToColumn(String type) {
        switch (type) {
            case "state":
                return LocationGroupType.STATE;
            case "city":
                return LocationGroupType.CITY;
            case "town":
                return LocationGroupType.TOWN;
            default:
                throw new PhotoException(PhotoErrorResult.INVALID_LOCATION_GROUP_TYPE);
        }
    }
}
