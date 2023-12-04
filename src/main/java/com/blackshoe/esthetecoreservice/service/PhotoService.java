package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.vo.LocationGroupType;
import com.blackshoe.esthetecoreservice.vo.PhotoAddressFilter;
import com.blackshoe.esthetecoreservice.vo.PhotoPointFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PhotoService {
    PhotoDto uploadPhotoToS3(UUID userId, MultipartFile photo, PhotoDto.CreatePhotoRequest photoUploadRequest);

    PhotoDto.DeletePhotoResponse deletePhoto(UUID photoId);
  
    PhotoDto.GetResponse getPhoto(UUID photoId);
  
    PhotoDto.GetGenresResponse getGenres();

    Page<PhotoDto.ReadRegionGroupResponse> getTop10ByUserLocationGroupBy(PhotoPointFilter photoPointFilter,
                                                                         LocationGroupType locationGroupType);

    Page<PhotoDto.ReadPhotoResponse> readByAddress(PhotoAddressFilter photoAddressFilter, Integer page, Integer size, Sort sort);


}
