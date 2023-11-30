package com.blackshoe.esthetecoreservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class NewWorkDto {

    /*
     ”photographer_id” : “” ”profile_img” : “”, ”nickname” : “”, ”has_new” : boolean,
     */
    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadNewWorkResponse {
        private String photographerId;
        private String profileImg;
        private String nickname;
        private String photoId;
        private String exhibitionId;
        private boolean hasNewPhoto;
        private boolean hasNewExhibition;
        private String updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UpdateViewOfPhotoRequest {
        private String userId;
        private String photoId;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UpdateViewOfExhibitionRequest {
        private String userId;
        private String exhibitionId;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UpdateNewWorkResponse {
        private String updatedAt;
    }
}
