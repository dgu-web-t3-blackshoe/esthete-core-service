package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.dto.PhotoUrlDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PhotoService {
    PhotoDto uploadPhotoToS3(MultipartFile photo, PhotoDto.CreateRequest photoUploadRequest);

    PhotoDto.DeleteResponse deletePhoto(UUID photoId);
  
    PhotoDto.GetResponse getPhoto(UUID photoId);
  
    PhotoDto.GetGenresResponse getGenres();
}
