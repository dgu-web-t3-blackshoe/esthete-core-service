package com.blackshoe.esthetecoreservice.dto;

import com.blackshoe.esthetecoreservice.entity.PhotoUrl;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
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
    public static class PhotoUploadRequest {

        @NotNull(message = "title is required")
        private String title;

        @NotNull(message = "description is required")
        private String description;

        @NotNull(message = "detail is required")
        private String detail;

        @Pattern(regexp = "^(true|false)$", message = "is_public must be true or false")
        private String isPublic;
        PhotoUrl photoUrl;
    }

    @Data
    @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PhotoUploadResponse {
        private String photoId;
        private String createdAt;
    }


}
