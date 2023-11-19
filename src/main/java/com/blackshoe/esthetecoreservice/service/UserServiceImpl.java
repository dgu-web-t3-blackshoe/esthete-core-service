package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.dto.UserDto;
import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.repository.ExhibitionRepository;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ExhibitionRepository exhibitionRepository;

    @Override
    public UserDto.ReadBasicInfoResponse readBasicInfo(UUID userId) {

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        UserDto.ReadBasicInfoResponse userReadBasicInfoResponse = UserDto.ReadBasicInfoResponse.builder()
                .userId(user.getUserId().toString())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImgUrl().getCloudfrontUrl())
                .build();

        return userReadBasicInfoResponse;
    }

    @Override
    public ExhibitionDto.ReadCurrentOfUserResponse readCurrentExhibitionOfUser(UUID userId) {

        Exhibition exhibition = exhibitionRepository.findMostRecentExhibitionOfUser(userId)
                .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.EXHIBITION_NOT_FOUND));

        final ExhibitionDto.ReadCurrentOfUserResponse exhibitionReadCurrentOfUserResponse = ExhibitionDto.ReadCurrentOfUserResponse.builder()
                .exhibitionId(exhibition.getExhibitionId().toString())
                .title(exhibition.getTitle())
                .description(exhibition.getDescription())
                .thumbnail(exhibition.getThumbnail())
                .build();

        return exhibitionReadCurrentOfUserResponse;
    }

    @Override
    public List<UserDto.ReadUserPhotosResponse> readUserPhotos(UUID userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));
        List<Photo> photos = user.getPhotos();

        List<UserDto.ReadUserPhotosResponse> contents = new ArrayList<>();
        for(Photo photo : photos) {
            UserDto.ReadUserPhotosResponse userReadUserPhotosResponse = UserDto.ReadUserPhotosResponse.builder()
                    .userId(user.getUserId().toString())
                    .nickname(user.getNickname())
                    .photoId(photo.getPhotoId().toString())
                    .photoUrl(photo.getPhotoUrl().getCloudfrontUrl())
                    .createdAt(photo.getCreatedAt().toString())
                    .build();
            contents.add(userReadUserPhotosResponse);
        }

        return contents;
    }
}
