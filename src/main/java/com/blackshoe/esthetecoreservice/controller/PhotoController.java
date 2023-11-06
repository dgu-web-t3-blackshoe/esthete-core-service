package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.dto.PhotoUrlDto;
import com.blackshoe.esthetecoreservice.dto.ResponseDto;
import com.blackshoe.esthetecoreservice.service.PhotoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//objectMapper
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/core/photo") @Slf4j @RestController
public class PhotoController {

    private final ObjectMapper objectMapper;
    private final PhotoService photoService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDto> uploadPhoto(@RequestPart(name = "photo") MultipartFile photo,
                                                  @RequestPart(name = "photo_upload_request")
                                                  PhotoDto.PhotoUploadRequest photoUploadRequest) {
        //log all
        log.info("photoUploadRequest: {}", photoUploadRequest);

        //@TODO user 포함되게 수정
        final PhotoDto photoDto = photoService.uploadPhotoToS3(photo, photoUploadRequest);


        final PhotoDto.PhotoUploadResponse photoUploadResponse = PhotoDto.PhotoUploadResponse.builder()
                .photoId(photoDto.getPhotoId().toString())
                .createdAt(photoDto.getCreatedAt().toString())
                .build();

        final ResponseDto responseDto = ResponseDto.builder()
                .payload(objectMapper.convertValue(photoUploadResponse, Map.class))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping({"/{photo_id}/url"})
    public ResponseEntity<ResponseDto> getPhotoUrl(@PathVariable(name = "photo_id") UUID photoId) {
        final PhotoDto.GetPhotoUrlResponse getPhotoUrlResponse = photoService.getPhotoUrl(photoId);

        final ResponseDto responseDto = ResponseDto.builder()
                .payload(objectMapper.convertValue(getPhotoUrlResponse, Map.class))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}