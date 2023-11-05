package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.dto.PhotoUrlDto;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {

    PhotoDto uploadPhotoToS3(MultipartFile photo, PhotoDto.PhotoUploadRequest photoUploadRequest);
}
