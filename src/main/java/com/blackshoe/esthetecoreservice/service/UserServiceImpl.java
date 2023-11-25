package com.blackshoe.esthetecoreservice.service;

//import UserDto

import com.blackshoe.esthetecoreservice.dto.*;
import com.blackshoe.esthetecoreservice.entity.*;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final GuestBookRepository guestBookRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final UserEquipmentRepository userEquipmentRepository;

    private final KafkaUserInfoProducerService kafkaUserInfoProducerService;
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
    public Page<PhotoDto.ReadResponse> readUserPhotos(UUID userId, Sort sortBy, int page, int size) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size, sortBy);

        Page<PhotoDto.ReadResponse> photoReadResponses = photoRepository.findByUserOrderByCreatedAtDesc(user, pageable);

        return photoReadResponses;
    }

    @Override
    public Page<ExhibitionDto.ReadResponse> readUserExhibitions(UUID userId, Sort sortBy, int page, int size) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size, sortBy);

        Page<ExhibitionDto.ReadResponse> exhibitionReadResponses = exhibitionRepository.findByUserOrderByCreatedAtDesc(user, pageable);

        return exhibitionReadResponses;
    }

    @Override
    public Page<GuestBookDto.ReadResponse> readUserGuestbooks(UUID userId, Sort sortBy, int page, int size) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size, sortBy);

        Page<GuestBookDto.ReadResponse> guestBookReadResponses = guestBookRepository.findByUserOrderByCreatedAtDesc(user, pageable);

        return guestBookReadResponses;
    }

    public Page<UserDto.SearchResult> readAllNicknameContaining(String nickname, Pageable pageable) {

        Page<UserDto.SearchResult> userReadAllNicknameContainingResponse = userRepository.findAllByNicknameContaining(nickname, pageable);

        return userReadAllNicknameContainingResponse;
    }

    @Override
    public Page<UserDto.SearchResult> readAllGenresContaining(List<UUID> genres, Pageable pageable) {

        Page<UserDto.SearchResult> userReadAllGenresContainingResponse = userRepository.findAllByGenresContaining(genres, pageable);

        return userReadAllGenresContainingResponse;
    }

    @Override
    public Page<UserDto.SearchResult> readAllNicknameAndGenreContaining(String nickname, List<UUID> genres, Pageable pageable) {

        Page<UserDto.SearchResult> userReadAllNicknameAndGenreContainingResponse = userRepository.findAllByNicknameAndGenresContaining(nickname, genres, pageable);

        return userReadAllNicknameAndGenreContainingResponse;
    }

    @Override
    public UserDto.DeleteResponse deleteUser(UUID userId) {

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        userRepository.delete(user);

        UserDto.DeleteResponse userDeleteResponse = UserDto.DeleteResponse.builder()
                .userId(user.getUserId().toString())
                .deletedAt(LocalDateTime.now().toString())
                .build();

        kafkaUserInfoProducerService.deleteUser(UserDto.UserInfoDto.builder()
                .userId(user.getUserId())
                .build());

        return userDeleteResponse;
    }

    @Override
    public UserDto.SignUpInfoResponse signUp(UUID userId, UserDto.SignUpInfoRequest signUpInfoRequest) {

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        // GenreDto to UserGenre
// GenreDto to UserGenre
        List<UserGenre> genres = signUpInfoRequest.getGenres().stream()
                .map(genre -> {
                    // UserDto.GenreDto를 UserGenre로 변환
                    UserGenre userGenre = UserGenre.builder()
                            .user(user)
                            .genre(new Genre(UUID.fromString(genre.getGenreId()), genre.getGenre()))
                            .build();
                    return userGenre;
                })
                .collect(Collectors.toList());

        List<UserEquipment> equipments = signUpInfoRequest.getEquipmentNames().stream()
                .map(equipmentName -> UserEquipment.builder()
                        .user(user)
                        .equipmentName(equipmentName)
                        .build())
                .collect(Collectors.toList());


        User updatedUser = user.toBuilder()
                .nickname(signUpInfoRequest.getNickname())
                .biography(signUpInfoRequest.getBiography())
                .userGenres(genres)
                .userEquipments(equipments)
                .build();

        userRepository.save(updatedUser);

        return UserDto.SignUpInfoResponse.builder().userId(updatedUser.getUserId().toString()).createdAt(String.valueOf(LocalDateTime.now())).build();
    }

    @Override
    public UserDto.MyProfileInfoResponse getMyProfileInfo(UUID userId) {
        log.info("getMyProfileInfo userId: {}", userId.toString());
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        //@TODO: kafka 활용 시 삭제할 block
        if(user.getProfileImgUrl() == null) {
            ProfileImgUrl profileImgUrl = ProfileImgUrl.builder()
                    .cloudfrontUrl("")
                    .s3Url("")
                    .build();
            user = user.toBuilder()
                    .profileImgUrl(profileImgUrl)
                    .build();
        }

        UserDto.MyProfileInfoResponse myProfileInfoResponse = UserDto.MyProfileInfoResponse.builder()
                .userId(user.getUserId().toString())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImgUrl().getCloudfrontUrl())
                .biography(user.getBiography())
                .updatedAt(user.getUpdatedAt().toString())
                .build();

        return myProfileInfoResponse;
    }

    @Override
    public UserDto.SetMyProfileImgResponse setMyProfileImg(UUID userId, MultipartFile profileImg) {

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        ProfileImgUrl userProfileImgUrl = user.getProfileImgUrl();

        ProfileImgUrl profileImgUrl;

        ProfileImgUrlDto profileImgUrlDto = ProfileImgUrlDto.builder()
                .cloudfrontUrl(userProfileImgUrl.getCloudfrontUrl())
                .s3Url(userProfileImgUrl.getS3Url())
                .build();

        if(userProfileImgUrl.getCloudfrontUrl().equals("")) {
            profileImgUrl = ProfileImgUrl.builder()
                    .cloudfrontUrl("")
                    .s3Url("")
                    .build();
        }else{
            profileImgUrl = ProfileImgUrl.convertProfileImgUrlDtoToEntity(profileImgUrlDto);
        }

        User updatedUser = user.toBuilder()
                .profileImgUrl(profileImgUrl)
                .build();

        userRepository.save(updatedUser);

        UserDto.SetMyProfileImgResponse setMyProfileImgResponse = UserDto.SetMyProfileImgResponse.builder()
                .userId(updatedUser.getUserId().toString())
                .profileImg(updatedUser.getProfileImgUrl().getCloudfrontUrl())
                .updatedAt(updatedUser.getUpdatedAt().toString())
                .build();

        return setMyProfileImgResponse;
    }

    @Override
    public UserDto.UpdateMyProfileResponse updateMyProfile(UUID userId, UserDto.UpdateMyProfileRequest updateMyProfileRequest) {

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        List<UserGenre> genres = updateMyProfileRequest.getGenres().stream()
                .map(genre -> {
                    // UserDto.GenreDto를 UserGenre로 변환
                    UserGenre userGenre = UserGenre.builder()
                            .user(user)
                            .genre(new Genre(UUID.fromString(genre.getGenreId()), genre.getGenre()))
                            .build();
                    return userGenre;
                })
                .collect(Collectors.toList());

        List<UserEquipment> equipments = updateMyProfileRequest.getEquipmentNames().stream()
                .map(equipmentName -> UserEquipment.builder()
                        .user(user)
                        .equipmentName(equipmentName)
                        .build())
                .collect(Collectors.toList());


        User updatedUser = user.toBuilder()
                .nickname(updateMyProfileRequest.getNickname())
                .biography(updateMyProfileRequest.getBiography())
                .userGenres(genres)
                .userEquipments(equipments)
                .build();

        userRepository.save(updatedUser);

        return UserDto.UpdateMyProfileResponse.builder().userId(updatedUser.getUserId().toString()).updatedAt(String.valueOf(LocalDateTime.now())).build();
    }

}
