package com.blackshoe.esthetecoreservice.dto;

import com.blackshoe.esthetecoreservice.entity.RoomPhoto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    public RoomPhotoDto(RoomPhoto roomPhoto) {
        this.roomId = roomPhoto.getRoom().getRoomId().toString();
        this.photoId = roomPhoto.getPhoto().getPhotoId().toString();
        this.title = roomPhoto.getPhoto().getTitle();
        this.photo = roomPhoto.getPhoto().getPhotoUrl().getCloudfrontUrl();
        this.userId = roomPhoto.getPhoto().getUser().getUserId().toString();
        this.nickname = roomPhoto.getPhoto().getUser().getNickname();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReadRoomPhotoListResponse {
        private List<RoomPhotoDto> roomPhotos;
    }
}
