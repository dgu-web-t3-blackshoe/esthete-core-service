package com.blackshoe.esthetecoreservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhotoAbusingReportDto {
        private String photoId;
        private String photoTitle;
        private String photoDescription;
        private String photoUrl;
        private String photoCreatedAt;
        private String photographerId;
        private String photographerNickname;
        private String photographerProfileImg;
        private String reporterId;
        private String reporterNickname;
        private String reporterProfileImg;
        private String reason;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class photoAbusingReportCreateRequest {
                @NotNull(message = "사진 ID를 입력해주세요.")
                @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}" +
                        "-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                        message = "올바른 사진 ID 형식이 아닙니다.")
                private String photoId;

                @NotNull(message = "신고자 ID를 입력해주세요.")
                @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}" +
                        "-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                        message = "올바른 신고자 ID 형식이 아닙니다.")
                private String userId;

                @NotNull(message = "신고 내용을 입력해주세요.")
                private String reason;
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class photoAbusingReportCreateResponse {
                private String photoAbusingReportId;
                private String createdAt;
        }
}
