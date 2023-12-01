package com.blackshoe.esthetecoreservice.service;
import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.dto.GuestBookDto;
import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto.ReadEquipmentsResponse getEquipmentsForUser(UUID userId);
    UserDto.ReadBasicInfoResponse readBasicInfo(UUID userId);

    ExhibitionDto.ReadCurrentOfUserExhibitionResponse readCurrentExhibitionOfUser(UUID userId);

    Page<PhotoDto.ReadPhotoResponse> readUserPhotos(UUID userId, Sort sortBy, int page, int size);

    Page<ExhibitionDto.ReadExhibitionResponse> readUserExhibitions(UUID userId, Sort sortBy, int page, int size);

    Page<GuestBookDto.ReadGuestBookResponse> readUserGuestbooks(UUID userId, Sort sortBy, int page, int size);
    Page<UserDto.SearchResult> readAllNicknameContaining(String nickname, Pageable pageable);

    Page<UserDto.SearchResult> readAllGenresContaining(List<UUID> genres, Pageable pageable);

    Page<UserDto.SearchResult> readAllNicknameAndGenreContaining(String nickname, List<UUID> genres, Pageable pageable);

    UserDto.DeleteResponse deleteUser(UUID userId);

    UserDto.SignUpResponse signUp(UUID userId, UserDto.SignUpRequest signUpInfoRequest);

    UserDto.MyProfileInfoResponse getMyProfileInfo(UUID userId);

    UserDto.UpdateProfileResponse updateMyProfile(UUID userId, UserDto.UpdateProfileRequest updateMyProfileRequest);
}
