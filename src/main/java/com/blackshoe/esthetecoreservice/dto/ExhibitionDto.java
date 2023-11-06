package com.blackshoe.esthetecoreservice.dto;

import com.blackshoe.esthetecoreservice.entity.Room;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
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
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CreateRequest {
        @NotNull(message = "전시 제목을 입력해주세요.")
        private String title;
        @NotNull(message = "전시 설명을 입력해주세요.")
        private String description;
        @NotNull(message = "전시 썸네일을 등록해주세요.")
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$\n",
                message = "올바른 사진 ID 형식이 아닙니다.")
        private String thumbnail;
    }

    @Data
    @Builder
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CreateResponse {
        private UUID exhibitionId;
        private LocalDateTime createdAt;
    }
}
