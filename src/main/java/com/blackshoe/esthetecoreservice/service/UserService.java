package com.blackshoe.esthetecoreservice.service;
import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto.ReadEquipmentsResponse getEquipmentsForUser(UUID userId);
    UserDto.ReadBasicInfoResponse readBasicInfo(UUID userId);

    ExhibitionDto.ReadCurrentOfUserResponse readCurrentExhibitionOfUser(UUID userId);

    List<UserDto.ReadUserPhotosResponse> readUserPhotos(UUID userId);

    List<UserDto.ReadUserExhibitionResponse> readUserExhibitions(UUID userId);

    List<UserDto.ReadUserGuestbookResponse> readUserGuestbooks(UUID userId);
}
