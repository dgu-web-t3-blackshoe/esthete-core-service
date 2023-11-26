package com.blackshoe.esthetecoreservice.service;


import com.blackshoe.esthetecoreservice.dto.ProfileImgUrlDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ProfileImgService {
    ProfileImgUrlDto uploadProfileImg(UUID userId, MultipartFile profileImg);
    void deleteProfileImg(UUID userId);

    ProfileImgUrlDto getUserPresentProfileImgUrlDto(UUID userId);
}
