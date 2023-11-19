package com.blackshoe.esthetecoreservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomPhotoDto {
    private String roomId;
    private String photoId;
    private String title;
    private String photo;
    private String userId;
    private String nickname;

    public RoomPhotoDto(UUID roomId, UUID photoId, String title, String photo, UUID userId, String nickname) {
        this.roomId = roomId.toString();
        this.photoId = photoId.toString();
        this.title = title;
        this.photo = photo;
        this.userId = userId.toString();
        this.nickname = nickname;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReadListResponse {
        private List<RoomPhotoDto> roomPhotos;
    }
}
