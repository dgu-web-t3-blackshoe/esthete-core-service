package com.blackshoe.esthetecoreservice.dto;

import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.Room;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class ExhibitionDto {
    private Long id;
    private UUID exhibitionId;
    private String title;
    private String description;
    private String thumbnail;
    private LocalDateTime createdAt;
    private List<Room> rooms;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CreateExhibitionRequest {

        @NotNull(message = "사용자 ID를 입력해주세요.")
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-" +
                "[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "올바른 사용자 ID 형식이 아닙니다.")
        private String userId;

        @NotNull(message = "전시 제목을 입력해주세요.")
        private String title;
        @NotNull(message = "전시 설명을 입력해주세요.")
        private String description;
        @NotNull(message = "전시 썸네일을 등록해주세요.")
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                message = "올바른 사진 ID 형식이 아닙니다.")
        private String thumbnail;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CreateExhibitionResponse {
        private String exhibitionId;
        private String createdAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DeleteExhibitionResponse {
        private String exhibitionId;
        private String deletedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReadRandomExhibitionResponse {
        private String exhibitionId;
        private String title;
        private String description;
        private String thumbnail;
        private String userId;
        private String nickname;
        private String profileImg;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReadCurrentOfUserExhibitionResponse {
        private String exhibitionId;
        private String title;
        private String description;
        private String thumbnail;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReadExhibitionResponse {
        private String exhibitionId;
        private String title;
        private String description;
        private String thumbnail;

        public ReadExhibitionResponse(Exhibition exhibition){
            this.exhibitionId = exhibition.getExhibitionId().toString();
            this.title = exhibition.getTitle() != null ? exhibition.getTitle() : "";
            this.description = exhibition.getDescription() != null ? exhibition.getDescription() : "";
            this.thumbnail = exhibition.getThumbnail() != null ? exhibition.getThumbnail() : "";
        }

        //get photo url from thumbnail(photoId)
        public ReadExhibitionResponse(Exhibition exhibition, String photoUrl){
            this.exhibitionId = exhibition.getExhibitionId().toString();
            this.title = exhibition.getTitle() != null ? exhibition.getTitle() : "";
            this.description = exhibition.getDescription() != null ? exhibition.getDescription() : "";
            this.thumbnail = photoUrl;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadRecommendedExhibitionResponse {
        private String exhibitionId;
        private String title;
        private String description;
        private String thumbnail;
        private String userId;
        private String nickname;
        private String profileImg;

        public ReadRecommendedExhibitionResponse(Exhibition exhibition){
            this.exhibitionId = exhibition.getExhibitionId().toString();
            this.title = exhibition.getTitle() != null ? exhibition.getTitle() : "";
            this.description = exhibition.getDescription() != null ? exhibition.getDescription() : "";
            this.thumbnail = exhibition.getThumbnail() != null ? exhibition.getThumbnail() : "";
            this.userId = exhibition.getUser().getUserId().toString();
            this.nickname = exhibition.getUser().getNickname();
            this.profileImg = exhibition.getUser().getProfileImgUrl().getCloudfrontUrl();
        }
    }

}
