package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.service.PhotoService;
import com.blackshoe.esthetecoreservice.vo.LocationGroupType;
import com.blackshoe.esthetecoreservice.vo.PhotoAddressFilter;
import com.blackshoe.esthetecoreservice.vo.PhotoPointFilter;
import com.blackshoe.esthetecoreservice.vo.PhotoSortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//objectMapper
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/core/photos")
@Slf4j
@RestController
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PhotoDto.UploadResponse> uploadPhoto(@RequestPart(name = "photo") MultipartFile photo,
                                                               @RequestPart(name = "photo_upload_request")
                                                               PhotoDto.CreateRequest photoUploadRequest) {
        //log all
        log.info("photoUploadRequest: {}", photoUploadRequest);

        //@TODO user 포함되게 수정
        final PhotoDto photoDto = photoService.uploadPhotoToS3(photo, photoUploadRequest);


        final PhotoDto.UploadResponse photoUploadResponse = PhotoDto.UploadResponse.builder()
                .photoId(photoDto.getPhotoId().toString())
                .createdAt(photoDto.getCreatedAt().toString())
                .build();

        return ResponseEntity.ok(photoUploadResponse);
    }

    @GetMapping({"/{photo_id}"})
    public ResponseEntity<PhotoDto.GetResponse> getPhoto(@PathVariable(name = "photo_id") UUID photoId) {
        final PhotoDto.GetResponse photoGetResponse = photoService.getPhoto(photoId);

        return ResponseEntity.ok(photoGetResponse);
    }

    @GetMapping({"/genres"})
    public ResponseEntity<PhotoDto.GetGenresResponse> getGenres() {
        final PhotoDto.GetGenresResponse photoGetGenresResponse = photoService.getGenres();

        return ResponseEntity.ok(photoGetGenresResponse);
    }

    @GetMapping("/locations/current")
    public ResponseEntity<Page<PhotoDto.ReadRegionGroupResponse>> getTop10ByUserLocationGroupBy(@RequestParam(name = "longitude") double longitude,
                                                                                                @RequestParam(name = "latitude") double latitude,
                                                                                                @RequestParam(name = "radius") double radius,
                                                                                                @RequestParam(name = "group") String group) {
        final PhotoPointFilter photoPointFilter = PhotoPointFilter.builder()
                .longitude(longitude)
                .latitude(latitude)
                .radius(radius)
                .build();

        final LocationGroupType locationGroupType = LocationGroupType.convertParamToColumn(group);

        final Page<PhotoDto.ReadRegionGroupResponse> photoReadRegionGroupResponse
                = photoService.getTop10ByUserLocationGroupBy(photoPointFilter, locationGroupType);

        return ResponseEntity.status(HttpStatus.OK).body(photoReadRegionGroupResponse);
    }

    @GetMapping("/locations")
    public ResponseEntity<Page<PhotoDto.ReadResponse>> readByAddress (
            @RequestParam(name = "state", required = true) Optional<String> state,
            @RequestParam(name = "city", required = false) Optional<String> city,
            @RequestParam(name = "town", required = false) Optional<String> town,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "sort") String sort) {

        final PhotoAddressFilter photoAddressFilter = PhotoAddressFilter.builder()
                .state(state.orElse(""))
                .city(city.orElse(""))
                .town(town.orElse(""))
                .build();

        final Sort sortBy = PhotoSortType.convertParamToColumn(sort);

        final Page<PhotoDto.ReadResponse> photoReadByAddressResponse
                = photoService.readByAddress(photoAddressFilter, page, size, sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(photoReadByAddressResponse);
    }

}
