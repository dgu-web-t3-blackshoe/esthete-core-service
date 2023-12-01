package com.blackshoe.esthetecoreservice.service;

//import UserDto

import com.blackshoe.esthetecoreservice.dto.*;
import com.blackshoe.esthetecoreservice.entity.*;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.repository.*;
import com.blackshoe.esthetecoreservice.vo.UserSortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private final UserGenreRepository userGenreRepository;
    private final UserEquipmentRepository userEquipmentRepository;
    private final GenreRepository genreRepository;

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
                .equipments(equipmentNames)
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
    public ExhibitionDto.ReadCurrentOfUserExhibitionResponse readCurrentExhibitionOfUser(UUID userId) {

        Exhibition exhibition = exhibitionRepository.findMostRecentExhibitionOfUser(userId)
                .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.EXHIBITION_NOT_FOUND));

        final ExhibitionDto.ReadCurrentOfUserExhibitionResponse exhibitionReadCurrentOfUserExhibitionResponse = ExhibitionDto.ReadCurrentOfUserExhibitionResponse.builder()
                .exhibitionId(exhibition.getExhibitionId().toString())
                .title(exhibition.getTitle())
                .description(exhibition.getDescription())
                .thumbnail(exhibition.getThumbnail())
                .build();

        return exhibitionReadCurrentOfUserExhibitionResponse;
    }

    @Override
    public Page<PhotoDto.ReadPhotoResponse> readUserPhotos(UUID userId, Sort sortBy, int page, int size) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size, sortBy);

        Page<PhotoDto.ReadPhotoResponse> photoReadResponses = photoRepository.findByUserOrderByCreatedAtDesc(user, pageable);

        return photoReadResponses;
    }

    @Override
    public Page<ExhibitionDto.ReadExhibitionResponse> readUserExhibitions(UUID userId, Sort sortBy, int page, int size) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size, sortBy);

        Page<ExhibitionDto.ReadExhibitionResponse> exhibitionReadResponses = exhibitionRepository.findByUser(user, pageable);

        //get Thumbnail from exhibitionReadResponses
        exhibitionReadResponses.stream().forEach(exhibitionReadResponse -> {
            Photo photo = photoRepository.findByPhotoId(UUID.fromString(exhibitionReadResponse.getThumbnail()))
                    .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.EXHIBITION_NOT_FOUND));

            exhibitionReadResponse.setThumbnail(photo.getPhotoUrl().getCloudfrontUrl());
        });

        return exhibitionReadResponses;
    }

    @Override
    public Page<GuestBookDto.ReadGuestBookResponse> readUserGuestbooks(UUID userId, Sort sortBy, int page, int size) {
        //if sortBy is RECENT, then sortBy = createdAt
        if (sortBy.toString().contains(UserSortType.RECENT.getSortType())) {
            sortBy = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        Pageable pageable = PageRequest.of(page, size, sortBy);

        Page<GuestBookDto.ReadGuestBookResponse> guestBookReadResponses = guestBookRepository.findByUserOrderByCreatedAtDesc(userId, pageable);

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
    @Transactional
    public UserDto.SignUpResponse signUp(UUID userId, UserDto.SignUpRequest signUpInfoRequest) {

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        signUpInfoRequest.getGenres().stream()
                .forEach(genreId -> {
                            Genre genre = genreRepository.findByGenreId(UUID.fromString(genreId))
                                    .orElseThrow(() -> new UserException(UserErrorResult.GENRE_NOT_FOUND));
                            UserGenre userGenre = UserGenre.builder()
                                    .user(user)
                                    .genre(genre)
                                    .build();

                            userGenreRepository.save(userGenre);
                        }
                );

        signUpInfoRequest.getEquipments().stream()
                .forEach(equipmentName -> {
                            UserEquipment userEquipment = UserEquipment.builder()
                                    .user(user)
                                    .equipmentName(equipmentName)
                                    .build();

                            userEquipmentRepository.save(userEquipment);
                        }
                );

        return UserDto.SignUpResponse.builder().userId(user.getUserId().toString()).createdAt(String.valueOf(LocalDateTime.now())).build();
    }

    @Override
    public UserDto.MyProfileInfoResponse getMyProfileInfo(UUID userId) {
        log.info("getMyProfileInfo userId: {}", userId.toString());
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        UserDto.MyProfileInfoResponse myProfileInfoResponse = UserDto.MyProfileInfoResponse.builder()
                .userId(user.getUserId().toString())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImgUrl().getCloudfrontUrl())
                .biography(user.getBiography())
                .updatedAt(user.getUpdatedAt().toString())
                .genres(user.getUserGenres().stream()
                        .map(userGenre -> new UserDto.GenreDto(userGenre.getGenre().getGenreId(), userGenre.getGenre().getGenreName()))
                        .collect(Collectors.toList()))
                .equipments(user.getUserEquipments().stream()
                        .map(userEquipment -> userEquipment.getEquipmentName())
                        .collect(Collectors.toList()))
                //.highlights(user.getPhotos().stream()
                .build();

        return myProfileInfoResponse;
    }

    @Override
    public UserDto.UpdateProfileResponse updateMyProfile(UUID userId, UserDto.UpdateProfileRequest updateMyProfileRequest) {

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        user.setNickname(updateMyProfileRequest.getNickname());
        user.setBiography(updateMyProfileRequest.getBiography());

        List<UserDto.GenreDto> genreDtos = null;

        updateMyProfileRequest.getGenres().stream().forEach(genreId -> {
            Genre genre = genreRepository.findByGenreId(UUID.fromString(genreId))
                    .orElseThrow(() -> new UserException(UserErrorResult.GENRE_NOT_FOUND));

            UserDto.GenreDto genreDto = new UserDto.GenreDto(genre.getGenreId(), genre.getGenreName());

            genreDtos.add(genreDto);

            UserGenre userGenre = UserGenre.builder()
                    .genre(genre)
                    .build();

            userGenre.setUser(user);

            userGenreRepository.save(userGenre);
        });

        updateMyProfileRequest.getEquipments().stream().forEach(equipmentName -> {
            UserEquipment userEquipment = UserEquipment.builder()
                    .equipmentName(equipmentName)
                    .build();

            userEquipment.setUser(user);

            userEquipmentRepository.save(userEquipment);
        });

        UserDto.UpdateProfileResponse updateMyProfileResponse = UserDto.UpdateProfileResponse.builder()
                .userId(user.getUserId().toString())
                .genres(genreDtos)
                .updatedAt(user.getUpdatedAt().toString())
                .build();

        return updateMyProfileResponse;
    }

}
