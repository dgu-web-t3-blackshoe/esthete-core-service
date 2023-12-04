package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.GuestBookAbusingReportDto;
import com.blackshoe.esthetecoreservice.dto.PhotoAbusingReportDto;
import com.blackshoe.esthetecoreservice.entity.GuestBook;
import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.exception.AbusingReportErrorResult;
import com.blackshoe.esthetecoreservice.exception.AbusingReportException;
import com.blackshoe.esthetecoreservice.repository.GuestBookRepository;
import com.blackshoe.esthetecoreservice.repository.PhotoRepository;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbusingReportServiceImpl implements AbusingReportService {

    private final GuestBookRepository guestBookRepository;

    private final PhotoRepository photoRepository;

    private final UserRepository userRepository;

    @Value("${services.admin-service")
    private String ADMIN_SERVICE;

    @Override
    public GuestBookAbusingReportDto.guestBookAbusingCreateResponse createGuestBookAbusingReport(
            GuestBookAbusingReportDto.guestBookAbusingCreateRequest guestBookAbusingReportCreateRequest) {

        final UUID guestBookId = UUID.fromString(guestBookAbusingReportCreateRequest.getGuestBookId());
        final UUID reporterId = UUID.fromString(guestBookAbusingReportCreateRequest.getUserId());

        final GuestBook guestBook = guestBookRepository.findByGuestBookId(guestBookId)
                .orElseThrow(() -> new AbusingReportException(AbusingReportErrorResult.GUEST_BOOK_NOT_FOUND));
        final User reporter = userRepository.findByUserId(reporterId)
                .orElseThrow(() -> new AbusingReportException(AbusingReportErrorResult.USER_NOT_FOUND));

        final GuestBookAbusingReportDto guestBookAbusingReportDto = GuestBookAbusingReportDto.builder()
                .guestBookId(guestBook.getGuestBookId().toString())
                .photographerId(guestBook.getPhotographer().getUserId().toString())
                .photographerNickname(guestBook.getPhotographer().getNickname())
                .photographerProfileImg(guestBook.getPhotographer().getProfileImgUrl() != null ? guestBook.getPhotographer().getProfileImgUrl().getCloudfrontUrl() : "")
                .guestBookId(guestBook.getGuestBookId().toString())
                .guestBookAuthorId(guestBook.getUser().getUserId().toString())
                .guestBookAuthorNickname(guestBook.getUser().getNickname())
                .guestBookAuthorProfileImg(guestBook.getUser().getProfileImgUrl() != null ? guestBook.getUser().getProfileImgUrl().getCloudfrontUrl() : "")
                .guestBookContent(guestBook.getContent())
                .guestBookCreatedAt(guestBook.getCreatedAt().toString())
                .reporterId(reporterId.toString())
                .reporterNickname(reporter.getNickname())
                .reporterProfileImg(reporter.getProfileImgUrl() != null ? reporter.getProfileImgUrl().getCloudfrontUrl() : "")
                .reason(guestBookAbusingReportCreateRequest.getReason())
                .build();

        WebClient webClient = WebClient.builder()
                .baseUrl(ADMIN_SERVICE)
                .build();

        webClient.post()
                .uri("/abusing-reports/guest-books")
                .bodyValue(guestBookAbusingReportDto)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    throw new AbusingReportException(AbusingReportErrorResult.ABUSING_REPORT_FAILED);
                })
                .bodyToMono(Void.class)
                .block();

        final GuestBookAbusingReportDto.guestBookAbusingCreateResponse guestBookAbusingReportCreateResponse
                = GuestBookAbusingReportDto.guestBookAbusingCreateResponse.builder()
                .guestBookAbusingReportId(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now().toString())
                .build();

        return guestBookAbusingReportCreateResponse;
    }

    @Override
    public PhotoAbusingReportDto.photoAbusingReportCreateResponse createPhotoAbusingReport(
            PhotoAbusingReportDto.photoAbusingReportCreateRequest photoBookAbusingReportCreateRequest) {

        final UUID photoId = UUID.fromString(photoBookAbusingReportCreateRequest.getPhotoId());
        final UUID reporterId = UUID.fromString(photoBookAbusingReportCreateRequest.getUserId());

        final Photo photo = photoRepository.findByPhotoId(photoId)
                .orElseThrow(() -> new AbusingReportException(AbusingReportErrorResult.PHOTO_NOT_FOUND));
        final User reporter = userRepository.findByUserId(reporterId)
                .orElseThrow(() -> new AbusingReportException(AbusingReportErrorResult.USER_NOT_FOUND));

        final PhotoAbusingReportDto photoAbusingReportDto = PhotoAbusingReportDto.builder()
                .photoId(photo.getPhotoId().toString())
                .photoTitle(photo.getTitle())
                .photoDescription(photo.getDescription())
                .photoUrl(photo.getPhotoUrl().getCloudfrontUrl())
                .photoCreatedAt(photo.getCreatedAt().toString())
                .photographerId(photo.getUser().getUserId().toString())
                .photographerNickname(photo.getUser().getNickname())
                .photographerProfileImg(photo.getUser().getProfileImgUrl() != null ? photo.getUser().getProfileImgUrl().getCloudfrontUrl() : "")
                .photoUrl(photo.getPhotoUrl().getCloudfrontUrl())
                .reporterId(reporterId.toString())
                .reporterNickname(reporter.getNickname())
                .reporterProfileImg(reporter.getProfileImgUrl() != null ? reporter.getProfileImgUrl().getCloudfrontUrl() : "")
                .reason(photoBookAbusingReportCreateRequest.getReason())
                .build();

        WebClient webClient = WebClient.builder()
                .baseUrl(ADMIN_SERVICE)
                .build();

        webClient.post()
                .uri("/abusing-reports/photos")
                .bodyValue(photoAbusingReportDto)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    throw new AbusingReportException(AbusingReportErrorResult.ABUSING_REPORT_FAILED);
                })
                .bodyToMono(Void.class)
                .block();

        final PhotoAbusingReportDto.photoAbusingReportCreateResponse photoBookAbusingReportCreateResponse
                = PhotoAbusingReportDto.photoAbusingReportCreateResponse.builder()
                .photoAbusingReportId(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now().toString())
                .build();

        return photoBookAbusingReportCreateResponse;
    }
}
