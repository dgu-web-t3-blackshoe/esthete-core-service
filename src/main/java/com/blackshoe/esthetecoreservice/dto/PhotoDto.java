package com.blackshoe.esthetecoreservice.dto;

import com.blackshoe.esthetecoreservice.entity.PhotoEquipment;
import com.blackshoe.esthetecoreservice.entity.Genre;
import com.blackshoe.esthetecoreservice.entity.PhotoLocation;
import com.blackshoe.esthetecoreservice.entity.PhotoUrl;
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
    public static class UploadRequest {

        @NotNull(message = "title is required")
        private String title;

        @NotNull(message = "description is required")
        private String description;

        @NotNull(message = "longitude is required")
        private double longitude;

        @NotNull(message = "latitude is required")
        private double latitude;

        @NotNull(message = "state is required")
        private String state;

        @NotNull(message = "city is required")
        private String city;

        @NotNull(message = "town is required")
        private String town;

        @NotNull(message = "time is required")
        private LocalDateTime time;

        @NotNull(message = "genres are required")
        private List<Long> genreIds;

        @NotNull(message = "equipments are required")
        private List<PhotoEquipmentDto> equipmentNames;
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
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetResponse{
        private String photoId;
        private String title;
        private String description;
        private LocalDateTime time;
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
    public static class PhotoEquipmentDto{
        private String equipmentName;

    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GenreIdsRequest{
        private List<Long> genreIds;
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
}
