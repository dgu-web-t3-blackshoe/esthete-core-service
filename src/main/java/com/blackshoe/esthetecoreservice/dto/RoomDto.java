package com.blackshoe.esthetecoreservice.dto;

import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.Room;
import com.blackshoe.esthetecoreservice.entity.RoomPhoto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomDto {
    private String roomId;
    private String title;
    private String description;
    private String thumbnail;
    private String createdAt;

    public RoomDto(Room room) {
        this.roomId = room.getRoomId().toString();
        this.title = room.getTitle();
        this.description = room.getDescription();
        this.thumbnail = room.getThumbnail();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CreateRequest {
        @NotNull(message = "전시실 제목을 입력해주세요.")
        private String title;
        @NotNull(message = "전시실 설명을 입력해주세요.")
        private String description;
        @NotNull(message = "전시실 썸네일을 등록해주세요.")
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                message = "올바른 사진 ID 형식이 아닙니다.")
        private String thumbnail;
        @NotNull(message = "전시실 사진을 등록해주세요.")
        private List<
                @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                        message = "올바른 사진 ID 형식이 아닙니다.")
                String> photos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CreateResponse {
        private String roomId;
        private String createdAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DeleteResponse {
        private String roomId;
        private String deletedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReadListResponse {
        private List<RoomDto> rooms;
    }
}
