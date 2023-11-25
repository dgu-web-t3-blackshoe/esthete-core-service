package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.dto.PhotoUrlDto;
import com.blackshoe.esthetecoreservice.vo.LocationGroupType;
import com.blackshoe.esthetecoreservice.vo.PhotoLocationFilter;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PhotoService {
    PhotoDto uploadPhotoToS3(MultipartFile photo, PhotoDto.CreateRequest photoUploadRequest);

    PhotoDto.DeleteResponse deletePhoto(UUID photoId);
  
    PhotoDto.GetResponse getPhoto(UUID photoId);
  
    PhotoDto.GetGenresResponse getGenres();

    Page<PhotoDto.ReadRegionGroupResponse> getTop10ByUserLocationGroupBy(PhotoLocationFilter photoLocationFilter,
                                                                         LocationGroupType locationGroupType);
}
