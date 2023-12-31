package com.blackshoe.esthetecoreservice.dto;

import com.blackshoe.esthetecoreservice.entity.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PhotoDto {

    private UUID userId;
    private UUID photoId;
    private String title;
    private String description;
    private String detail;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private PhotoUrl photoUrl;

    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @NoArgsConstructor @AllArgsConstructor
    public static class CreatePhotoRequest {

        private String title;

        private String description;

        private double longitude;

        private double latitude;

        private String isPublic;

        List<String> genreIds;

        private String state;

        private String city;

        private String town;

        private String time;

        private List<String> equipments;
    }

    @Data
    @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CreatePhotoResponse {
        private String photoId;
        private String createdAt;
    }

    @Data
    @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetUrlResponse{
        private String cloudfrontUrl;
        private String title;
        private String description;
        private String createdAt;
    }

    @Data
    @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DeletePhotoRequest {

        @NotNull(message = "userId is required")
        private UUID userId;
        private UUID photoId;
    }

    @Data
    @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DeletePhotoResponse {
        private String photoId;
        private String deletedAt;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetResponse{
        private String photoId;
        private String title;
        private String description;
        private String time;
        private LocationRequest photoLocation;
        private UrlRequest photoUrl;
        private List<String> equipments;
        private List<GenreDto> genres;
        private Long viewCount;
        private String createdAt;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class EquipmentNamesRequest {
        private List<String> equipmentNames;
    }


    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LocationRequest{
        private double longitude;
        private double latitude;
        private String state;
        private String city;
        private String town;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UrlRequest{
        private String cloudfrontUrl;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetGenresResponse{
        private List<GenreDto> genres;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GenreDto{
        private String genreId;
        private String genreName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadPhotoResponse {
        private String photoId;
        private String title;
        private String photoUrl;
        private String userId;
        private String nickname;
        private String time;
        private String createdAt;

        public ReadPhotoResponse(Photo photo){
            this.photoId = photo.getPhotoId().toString();
            this.title = photo.getTitle();
            this.photoUrl = photo.getPhotoUrl() != null ? photo.getPhotoUrl().getCloudfrontUrl() : "";
            this.userId = photo.getUser() != null ? photo.getUser().getUserId().toString() : "";
            this.nickname = photo.getUser() != null ? photo.getUser().getNickname() : "";
            this.time = photo.getUser() != null ? photo.getTime() : "";
            this.createdAt = photo.getCreatedAt() != null ? photo.getCreatedAt().toString() : "";
        }
    }

    @Data @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReadRegionGroupResponse {
        private String state;
        private String city;
        private String town;
        private String thumbnail;
        private Long count;

        @Builder
        public ReadRegionGroupResponse(String state,
                                       String city,
                                       String town,
                                       String thumbnail,
                                       Long count) {
            this.state = state;
            this.city = city;
            this.town = town;
            this.thumbnail = thumbnail;
            this.count = count;
        }

        @Builder
        public ReadRegionGroupResponse(String state,
                                       String city,
                                       String thumbnail,
                                       Long count) {
            this.state = state;
            this.city = city;
            this.town = null;
            this.thumbnail = thumbnail;
            this.count = count;
        }

        @Builder
        public ReadRegionGroupResponse(String state,
                                       String thumbnail,
                                       Long count) {
            this.state = state;
            this.city = null;
            this.town = null;
            this.thumbnail = thumbnail;
            this.count = count;
        }
    }
}
