package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.service.PhotoChecksumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/core/copyright")
@RequiredArgsConstructor
public class CopyrightController {

    private final PhotoChecksumService photoChecksumService;

    @PostMapping
    public ResponseEntity checkCopyRight(@RequestPart MultipartFile request) {

        photoChecksumService.validatePhotoChecksumExist(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
