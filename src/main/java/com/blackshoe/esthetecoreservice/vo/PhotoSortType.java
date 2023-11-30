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
public enum PhotoSortType {
    TRENDING("viewCount"),
    RECENT("createdAt");

    private final String sortType;

    public static Sort convertParamToColumn(String sort) {
        switch (sort) {
            case "trending":
                return Sort.by(Sort.Direction.DESC, TRENDING.sortType);
            case "recent":
                return Sort.by(Sort.Direction.DESC, RECENT.sortType);
            default:
                throw new PhotoException(PhotoErrorResult.INVALID_SORT_TYPE);
        }
    }
}
