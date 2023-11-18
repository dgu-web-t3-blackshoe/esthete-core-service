package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.dto.PhotoUrlDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PhotoService {
    PhotoDto uploadPhotoToS3(MultipartFile photo, PhotoDto.PhotoUploadRequest photoUploadRequest);

    PhotoDto.GetPhotoUrlResponse getPhotoUrl(UUID photoId);

    PhotoDto.DeleteResponse deletePhoto(UUID photoId);
}
