package com.blackshoe.esthetecoreservice.vo;

import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum UserSortType {
    POPULAR("supportCount"),
    TRENDING("viewCount"),
    RECENT("updatedAt"),
    ;

    private final String sortType;

    public static Sort convertParamToColumn(String sort) {
        switch (sort) {
            case "popular":
                return Sort.by(Sort.Direction.DESC, POPULAR.sortType);
            case "trending":
                return Sort.by(Sort.Direction.DESC, TRENDING.sortType);
            case "recent":
                return Sort.by(Sort.Direction.DESC, RECENT.sortType);
            default:
                throw new UserException(UserErrorResult.INVALID_SORT_TYPE);
        }
    }
}
