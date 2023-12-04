package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.GuestBookAbusingReportDto;
import com.blackshoe.esthetecoreservice.dto.PhotoAbusingReportDto;
import com.blackshoe.esthetecoreservice.service.AbusingReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("core/abusing-reports")
@RequiredArgsConstructor
public class AbusingReportController {

    private final AbusingReportService abusingReportService;

    @PostMapping("/guest-books")
    public ResponseEntity<GuestBookAbusingReportDto.guestBookAbusingCreateResponse>
    createGuestBookAbusingReport(@RequestBody GuestBookAbusingReportDto.guestBookAbusingCreateRequest guestBookAbusingReportCreateRequest) {

        GuestBookAbusingReportDto.guestBookAbusingCreateResponse guestBookAbusingCreateResponse
                = abusingReportService.createGuestBookAbusingReport(guestBookAbusingReportCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(guestBookAbusingCreateResponse);
    }

    @PostMapping("/photos")
    public ResponseEntity<PhotoAbusingReportDto.photoAbusingReportCreateResponse>
    createPhotoAbusingReport(@RequestBody PhotoAbusingReportDto.photoAbusingReportCreateRequest photoBookAbusingReportCreateRequest) {

        PhotoAbusingReportDto.photoAbusingReportCreateResponse photoAbusingReportCreateResponse
                = abusingReportService.createPhotoAbusingReport(photoBookAbusingReportCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(photoAbusingReportCreateResponse);
    }
}
