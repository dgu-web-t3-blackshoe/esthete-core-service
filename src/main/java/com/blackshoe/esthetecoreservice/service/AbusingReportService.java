package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.GuestBookAbusingReportDto;
import com.blackshoe.esthetecoreservice.dto.PhotoBookAbusingReportDto;

public interface AbusingReportService {
    GuestBookAbusingReportDto.guestBookAbusingCreateResponse
    createGuestBookAbusingReport(GuestBookAbusingReportDto.guestBookAbusingCreateRequest guestBookAbusingReportCreateRequest);
    PhotoBookAbusingReportDto.photoAbusingReportCreateResponse
    createPhotoAbusingReport(PhotoBookAbusingReportDto.photoAbusingReportCreateRequest photoBookAbusingReportCreateRequest);
}
