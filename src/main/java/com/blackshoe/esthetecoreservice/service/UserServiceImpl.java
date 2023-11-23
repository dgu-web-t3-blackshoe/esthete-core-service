package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.GuestBookDto;
import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.dto.UserDto;
import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.entity.UserEquipment;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.repository.*;
import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;
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
}
