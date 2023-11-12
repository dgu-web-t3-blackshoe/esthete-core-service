package com.blackshoe.esthetecoreservice.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.dto.PhotoUrlDto;
import com.blackshoe.esthetecoreservice.entity.*;
import com.blackshoe.esthetecoreservice.exception.PhotoException;
import com.blackshoe.esthetecoreservice.exception.PhotoErrorResult;
import com.blackshoe.esthetecoreservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PhotoServiceImpl implements PhotoService{

    private final AmazonS3Client amazonS3Client;
    private final PhotoRepository photoRepository;
    private final PhotoUrlRepository photoUrlRepository;
    private final PhotoLocationRepository photoLocationRepository;
    private final ViewRepository photoViewRepository;
    private final GenreRepository genreRepository;
    private final PhotoGenreRepository photoGenreRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET;
    @Value("${cloud.aws.cloudfront.distribution-domain}")
    private String DISTRIBUTION_DOMAIN;
    @Value("${cloud.aws.s3.root-directory}")
    private String ROOT_DIRECTORY;
    @Value("photo")
    private String PHOTO_DIRECTORY;

    //@TODO: user 포함한 로직
    @Transactional
    @Override
    public PhotoDto uploadPhotoToS3(MultipartFile photo, PhotoDto.UploadRequest photoUploadRequest) {
        if (photo == null) {
            throw new PhotoException(PhotoErrorResult.EMPTY_PHOTO);
        }

        //String s3FilePath = userId + "/" + PHOTO_DIRECTORY;
        String s3FilePath = PHOTO_DIRECTORY;
        String fileExtension = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf("."));

        UUID photoId = UUID.randomUUID();

        String key = ROOT_DIRECTORY + "/" + s3FilePath + "/" + photoId + fileExtension;

        //if (!ContentType.isContentTypeValid(photo.getContentType())) {
        //    throw new PhotoException(PhotoErrorResult.INVALID_SKIN_TYPE);
        //}

        if (photo.getSize() > 52428800) {
            throw new PhotoException(PhotoErrorResult.INVALID_PHOTO_SIZE);
        }

        try {
            amazonS3Client.putObject(BUCKET, key, photo.getInputStream(), null);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PhotoException(PhotoErrorResult.PHOTO_UPLOAD_FAILED);
        }

        String s3Url;

        try {
            s3Url = amazonS3Client.getUrl(BUCKET, key).toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PhotoException(PhotoErrorResult.PHOTO_UPLOAD_FAILED);
        }

        String cloudFrontUrl = DISTRIBUTION_DOMAIN + "/" + key;

        PhotoUrlDto photoUrlDto = PhotoUrlDto.builder()
                .s3Url(s3Url)
                .cloudfrontUrl(cloudFrontUrl)
                .build();

        PhotoUrl uploadedPhotoUrl = PhotoUrl.convertPhotoUrlDtoToEntity(photoUrlDto);

        PhotoLocation photoLocation = PhotoLocation.builder()
                .longitude(photoUploadRequest.getLongitude())
                .latitude(photoUploadRequest.getLatitude())
                .state(photoUploadRequest.getState())
                .city(photoUploadRequest.getCity())
                .town(photoUploadRequest.getTown())
                .build();


        List<PhotoDto.PhotoEquipmentDto> equipmentDtos = photoUploadRequest.getEquipmentNames();

        List<PhotoEquipment> photoEquipments = new ArrayList<>();

        equipmentDtos.forEach(equipment -> {
            PhotoEquipment newEquipment = PhotoEquipment.builder()
                    .photoEquipmentName(equipment.getEquipmentName())
                    .build();
            photoEquipments.add(newEquipment);
        });


        Photo uploadedPhoto = Photo.builder()
                .photoId(photoId)
                .photoUrl(uploadedPhotoUrl)
                .title(photoUploadRequest.getTitle())
                .description(photoUploadRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .time(photoUploadRequest.getTime())
                .photoLocation(photoLocation)
                .photoEquipments(photoEquipments)
                .build();

        photoRepository.save(uploadedPhoto);

        List<Long> genreIds  = photoUploadRequest.getGenreIds();

        for (long genreId : genreIds) {
            Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new PhotoException(PhotoErrorResult.GENRE_NOT_FOUND));

            PhotoGenre photoGenre = PhotoGenre.builder()
                    .photo(uploadedPhoto)
                    .genre(genre)
                    .build();

            photoGenreRepository.save(photoGenre);
        }

        PhotoDto photoDto = PhotoDto.builder()
                .photoId(photoId)
                .photoUrl(uploadedPhotoUrl)
                .createdAt(LocalDateTime.now())
                .build();

        return photoDto;
    }


    @Override
    @Transactional
    public PhotoDto.GetResponse getPhoto(UUID photoId) {
        Photo photo = photoRepository.findByPhotoId(photoId).orElseThrow(() -> new PhotoException(PhotoErrorResult.PHOTO_NOT_FOUND));

        List<PhotoGenre> photoGenres = photoGenreRepository.findByPhoto(photo).orElseThrow(() -> new PhotoException(PhotoErrorResult.PHOTO_GENRE_NOT_FOUND));

        PhotoDto.EquipmentIdsRequest equipmentNames = PhotoDto.EquipmentIdsRequest.builder()
                .equipmentNames(
                        photo.getPhotoEquipments()
                                .stream()
                                .map(equipment -> equipment.getPhotoEquipmentName())
                                .collect(Collectors.toList())  // 수정된 부분
                )
                .build();


        PhotoDto.LocationRequest locationRequest = PhotoDto.LocationRequest.builder()
                .longitude(photo.getPhotoLocation().getLongitude())
                .latitude(photo.getPhotoLocation().getLatitude())
                .state(photo.getPhotoLocation().getState())
                .city(photo.getPhotoLocation().getCity())
                .town(photo.getPhotoLocation().getTown())
                .build();

        PhotoDto.UrlRequest urlRequest = PhotoDto.UrlRequest.builder()
                .cloudfrontUrl(photo.getPhotoUrl().getCloudfrontUrl())
                .build();

        log.info("photo.getPhotoGenres(): {}", photo.getPhotoGenres());

        //PhotoGenre to Long
        List<String> genreIds = photoGenres
                .stream()
                .map(photoGenre -> String.valueOf(photoGenre.getGenre().getGenreId()))
                .collect(Collectors.toList());

        PhotoDto.GetResponse getPhotoResponse = PhotoDto.GetResponse.builder()
                .photoId(photo.getPhotoId().toString())
                .title(photo.getTitle())
                .description(photo.getDescription())
                .time(photo.getTime())
                .photoUrl(urlRequest)
                .photoLocation(locationRequest)
                .equipmentNames(equipmentNames)
                .genreIds(genreIds)
                .viewCount(photo.getViewCount())
                .createdAt(String.valueOf(photo.getCreatedAt()))
                .build();


        return getPhotoResponse;
    }

    @Override
    @Transactional
    public PhotoDto.GetGenresResponse getGenres() {
        List<Genre> genres = genreRepository.findAll();

        List<PhotoDto.GenreDto> genreDtos = genres
                .stream()
                .map(genre -> PhotoDto.GenreDto.builder()
                        .genreId(String.valueOf(genre.getGenreId()))
                        .genreName(genre.getGenreName())
                        .build())
                .collect(Collectors.toList());

        PhotoDto.GetGenresResponse getGenresResponse = PhotoDto.GetGenresResponse.builder()
                .genres(genreDtos)
                .build();

        return getGenresResponse;
    }
}
