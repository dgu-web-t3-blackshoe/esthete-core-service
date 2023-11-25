package com.blackshoe.esthetecoreservice.dto;

import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.entity.UserGenre;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserDto {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReadEquipmentsResponse {
        List<String> equipmentNames;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReadBasicInfoResponse {
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
    public static class SearchResult {
        private String photographerId;
        private String profileImg;
        private String nickname;
        private String biography;
        private List<GenreDto> genres;
        private List<HighlightDto> highlights;

        @Builder
        public SearchResult(User user) {
            this.photographerId = user.getUserId().toString();
            this.profileImg = user.getProfileImgUrl() != null ? user.getProfileImgUrl().getCloudfrontUrl() : "";
            this.nickname = user.getNickname();
            this.biography = user.getBiography();
            this.genres = user.getUserGenres() != null ? user.getUserGenres().stream()
                    .map(UserGenre::getGenre)
                    .map(genre -> new GenreDto(genre.getGenreId(), genre.getGenreName()))
                    .collect(Collectors.toList()) : new ArrayList<>();
            this.highlights = user.getPhotos() != null ? user.getPhotos().stream()
                    .map(photo -> new HighlightDto(photo.getPhotoId(), photo.getPhotoUrl().getCloudfrontUrl()))
                    .limit(10)
                    .collect(Collectors.toList()) : new ArrayList<>();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserInfoDto {
        private UUID userId;
        private String email;
        private String role;
    }

    @Getter
    public static class GenreDto {
        private String genreId;
        private String genre;

        public GenreDto(UUID genreId, String genre) {
            this.genreId = genreId.toString();
            this.genre = genre;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class HighlightDto {
        private String photoId;
        private String photo;

        public HighlightDto(UUID photoId, String photo) {
            this.photoId = photoId.toString();
            this.photo = photo;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DeleteResponse {
        private String userId;
        private String deletedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SignUpInfoRequest {
        private String nickname;
        private String biography;
        private List<GenreDto> genres;
        private List<String> equipmentNames;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SignUpInfoResponse {
        private String userId;
        private String createdAt;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UpdateRequest {
        private String nickname;
        private String biography;
        private List<GenreDto> genres;
        private List<String> equipmentNames;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UpdateProfileRequestDto{
        private String nickname;
        private String biography;
        private List<GenreDto> genres;
        private List<String> equipmentNames;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MyProfileInfoResponse{
        private String userId;
        private String nickname;
        private String profileImg;
        private String biography;
        private List<GenreDto> genres;
        private List<HighlightDto> highlights;
        private String updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SetMyProfileImgRequest{
        private String profileImg;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SetMyProfileImgResponse{

        private String userId;
        private String profileImg;
        private String updatedAt;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UpdateMyProfileRequest{
        private String nickname;
        private String biography;
        private List<GenreDto> genres;
        private List<String> equipmentNames;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class UpdateMyProfileResponse{
        private String userId;
        private String updatedAt;
    }
}
