package com.blackshoe.esthetecoreservice.dto;

import com.blackshoe.esthetecoreservice.entity.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    public static class UploadResponse {
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
    public static class DeleteRequest{

        @NotNull(message = "userId is required")
        private UUID userId;
        private UUID photoId;
    }

    @Data
    @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DeleteResponse {
        private String photoId;
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
        private EquipmentIdsRequest equipmentNames;
        private List<String> genreIds;
        private Long viewCount;
        private String createdAt;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GenreIdDto{
        private UUID genreId;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GenreIdsRequest{
        private List<String> genreIds;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class EquipmentIdsRequest{
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
    public static class ReadResponse{
        private String photoId;
        private String title;
        private String photoUrl;
        private String userId;
        private String nickname;
        private String time;
        private String createdAt;

        public ReadResponse(Photo photo){
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
    }
}
