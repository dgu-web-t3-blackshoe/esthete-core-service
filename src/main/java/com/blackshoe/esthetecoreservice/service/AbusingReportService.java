package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.GuestBookAbusingReportDto;
import com.blackshoe.esthetecoreservice.dto.PhotoAbusingReportDto;

public interface AbusingReportService {
    GuestBookAbusingReportDto.guestBookAbusingCreateResponse
    createGuestBookAbusingReport(GuestBookAbusingReportDto.guestBookAbusingCreateRequest guestBookAbusingReportCreateRequest);
    PhotoAbusingReportDto.photoAbusingReportCreateResponse
    createPhotoAbusingReport(PhotoAbusingReportDto.photoAbusingReportCreateRequest photoBookAbusingReportCreateRequest);
}
