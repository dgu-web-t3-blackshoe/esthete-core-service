package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.GuestBookDto;
import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.dto.UserDto;
import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.entity.GuestBook;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.entity.UserEquipment;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.repository.UserEquipmentRepository;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.repository.ExhibitionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ExhibitionRepository exhibitionRepository;
    private final UserEquipmentRepository userEquipmentRepository;
    @Override
    public UserDto.ReadEquipmentsResponse getEquipmentsForUser(UUID userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        List<UserEquipment> equipments = userEquipmentRepository.findByUser(user);

        List<String> equipmentNames = equipments.stream()
                .map(UserEquipment::getEquipmentName)
                .collect(Collectors.toList());

        //get equipment name from equipments

        return UserDto.ReadEquipmentsResponse.builder()
                .equipmentNames(equipmentNames)
                .build();
    }
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
    public List<PhotoDto.ReadResponse> readUserPhotos(UUID userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));
        List<Photo> photos = user.getPhotos();

        List<PhotoDto.ReadResponse> content = new ArrayList<>();
        for(Photo photo : photos) {
            PhotoDto.ReadResponse userReadUserPhotosResponse = PhotoDto.ReadResponse.builder()
                    .userId(user.getUserId().toString())
                    .nickname(user.getNickname())
                    .photoId(photo.getPhotoId().toString())
                    .photoUrl(photo.getPhotoUrl().getCloudfrontUrl())
                    .createdAt(photo.getCreatedAt().toString())
                    .build();
            content.add(userReadUserPhotosResponse);
        }

        return content;
    }

    @Override
    public List<ExhibitionDto.ReadResponse> readUserExhibitions(UUID userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));
        List<Exhibition> exhibitions = user.getExhibitions();

        List<ExhibitionDto.ReadResponse> content = new ArrayList<>();
        for(Exhibition exhibition : exhibitions) {
            ExhibitionDto.ReadResponse userReadUserExhibitionResponse = ExhibitionDto.ReadResponse.builder()
                    .exhibitionId(exhibition.getExhibitionId().toString())
                    .title(exhibition.getTitle())
                    .description(exhibition.getDescription())
                    .thumbnail(exhibition.getThumbnail())
                    .build();
            content.add(userReadUserExhibitionResponse);
        }

        return content;
    }

    @Override
    public List<GuestBookDto.ReadResponse> readUserGuestbooks(UUID userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        List<GuestBook> guestbooks = user.getGuestBooks();

        List<GuestBookDto.ReadResponse> content = new ArrayList<>();
        for(GuestBook guestbook : guestbooks) {
            GuestBookDto.ReadResponse userReadUserGuestbookResponse = GuestBookDto.ReadResponse.builder()
                    .guestbookId(guestbook.getGuestBookId().toString())
                    .createdAt(guestbook.getCreatedAt().toString())
                    .photographerId(userId.toString())
                    .userId(guestbook.getUser().getUserId().toString())
                    .nickname(guestbook.getUser().getNickname())
                    .content(guestbook.getContent())
                    .build();
            content.add(userReadUserGuestbookResponse);
        }

        return content;
    }
}
