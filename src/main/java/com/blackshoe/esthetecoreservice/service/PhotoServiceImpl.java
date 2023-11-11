package com.blackshoe.esthetecoreservice.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.dto.PhotoUrlDto;
import com.blackshoe.esthetecoreservice.entity.*;
import com.blackshoe.esthetecoreservice.exception.PhotoException;
import com.blackshoe.esthetecoreservice.exception.PhotoErrorResult;
import com.blackshoe.esthetecoreservice.repository.PhotoLocationRepository;
import com.blackshoe.esthetecoreservice.repository.PhotoRepository;
import com.blackshoe.esthetecoreservice.repository.PhotoUrlRepository;
import com.blackshoe.esthetecoreservice.repository.PhotoViewRepository;
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
    private final PhotoViewRepository photoViewRepository;

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

        List<PhotoDto.GenreDto> genreDtos = photoUploadRequest.getGenreIds();
        List<PhotoDto.EquipmentDto> equipmentDtos = photoUploadRequest.getEquipmentIds();

        List<Genre> genres = new ArrayList<>();

        genreDtos.forEach(genre -> {
            Genre newGenre = Genre.builder()
                    .genreId(genre.getGenreId())
                    .build();
            genres.add(newGenre);
        });

        List<Equipment> equipments = new ArrayList<>();

        equipmentDtos.forEach(equipment -> {
            Equipment newEquipment = Equipment.builder()
                    .equipmentId(equipment.getEquipmentId())
                    .build();
            equipments.add(newEquipment);
        });

        Photo uploadedPhoto = Photo.builder()
                .photoId(photoId)
                .photoUrl(uploadedPhotoUrl)
                .title(photoUploadRequest.getTitle())
                .description(photoUploadRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .time(photoUploadRequest.getTime())
                .photoLocation(photoLocation)
                .genres(genres)
                .equipments(equipments)
                .photoView(PhotoView.builder()
                        .photoId(photoId)
                        .build())
                .build();

        photoRepository.save(uploadedPhoto);

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

        long viewCount = photoViewRepository.countByPhotoId(photoId) - 1;

        PhotoDto.GenreIdsRequest genreIdsRequest = PhotoDto.GenreIdsRequest.builder()
                .genreIds(
                        photo.getGenres()
                                .stream()
                                .map(genre -> genre.getGenreId().toString())
                                .collect(Collectors.toList())  // 수정된 부분
                )
                .build();

        PhotoDto.EquipmentIdsRequest equipmentIdsRequest = PhotoDto.EquipmentIdsRequest.builder()
                .equipmentIds(
                        photo.getEquipments()
                                .stream()
                                .map(equipment -> equipment.getEquipmentId().toString())
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

        PhotoDto.GetResponse getPhotoResponse = PhotoDto.GetResponse.builder()
                .photoId(photo.getPhotoId().toString())
                .title(photo.getTitle())
                .description(photo.getDescription())
                .time(photo.getTime())
                .photoUrl(urlRequest)
                .photoLocation(locationRequest)
                .equipmentIds(equipmentIdsRequest)
                .genreIds(genreIdsRequest)
                .viewCount(viewCount)
                .createdAt(String.valueOf(photo.getCreatedAt()))
                .build();


        return getPhotoResponse;
    }
}
