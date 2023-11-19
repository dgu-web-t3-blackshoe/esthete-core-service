package com.blackshoe.esthetecoreservice.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.dto.PhotoUrlDto;
import com.blackshoe.esthetecoreservice.entity.*;
import com.blackshoe.esthetecoreservice.exception.PhotoException;
import com.blackshoe.esthetecoreservice.exception.PhotoErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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

    //redis
    private final RedisTemplate redisTemplate;

    private final NewWorkRepository newWorkRepository;
    private final UserRepository userRepository;
    private final SupportRepository supportRepository;

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

        User photographer = userRepository.findByUserId(photoUploadRequest.getUserId()).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        //String s3FilePath = userId + "/" + PHOTO_DIRECTORY;
        String s3FilePath = PHOTO_DIRECTORY;
        String fileExtension = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf("."));

        UUID photoId = UUID.randomUUID();

        String key = ROOT_DIRECTORY + "/" + s3FilePath + "/" + photoId + fileExtension;


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

        List<PhotoGenre> photoGenres = new ArrayList<>();

        for(String genreName : photoUploadRequest.getGenres()){

            Genre genre = genreRepository.findByGenreName(genreName).orElseThrow(() -> new PhotoException(PhotoErrorResult.GENRE_NOT_FOUND));

            photoGenres.add(PhotoGenre.builder()
                    .genre(genre)
                    .build());
        }
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
                .photoGenres(photoGenres)
                .time(photoUploadRequest.getTime())
                .photoLocation(photoLocation)
                .photoEquipments(photoEquipments)
                .build();

        photoRepository.save(uploadedPhoto);

        NewWork newWork = newWorkRepository.findByPhotographerId(photographer.getUserId());
        List<Support> supports = supportRepository.findAllByPhotographerId(photographer.getUserId());

        String[] userIdWithCondition;
        List<String[]> supporters = new ArrayList<>();

        for(Support support : supports){
            userIdWithCondition = new String[]{support.getUser().getUserId().toString(), "true"};
            supporters.add(userIdWithCondition);
        }

        String hasNewRedisKey = "photographer_" + photoUploadRequest.getUserId().toString() + "_photo_" + uploadedPhoto.getPhotoId().toString();
        redisTemplate.opsForValue().set(hasNewRedisKey, supporters.toString());
        redisTemplate.expire(hasNewRedisKey, 60 * 60 * 24, java.util.concurrent.TimeUnit.SECONDS);

        if(newWork == null){
            newWork = NewWork.builder()
                    .photo(uploadedPhoto)
                    .photographer(photographer)
                    .photographerId(UUID.fromString(photographer.getUserId().toString()))
                    .build();
        }
        else {
            newWork.setPhoto(uploadedPhoto);
        }

        newWorkRepository.save(newWork);

        List<UUID> genreIds  = photoUploadRequest.getGenres()
                .stream()
                .map(genreName -> genreRepository.findByGenreName(genreName).orElseThrow(() -> new PhotoException(PhotoErrorResult.GENRE_NOT_FOUND)).getGenreId())
                .collect(Collectors.toList());

        for (UUID genreId : genreIds) {

            Genre genre = genreRepository.findByGenreId(genreId).orElseThrow(() -> new PhotoException(PhotoErrorResult.GENRE_NOT_FOUND));

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

    @Override
    @Transactional
    public PhotoDto.DeleteResponse deletePhoto(UUID photoId) {
        Photo photo = photoRepository.findByPhotoId(photoId).orElseThrow(() -> new PhotoException(PhotoErrorResult.PHOTO_NOT_FOUND));

        photoRepository.delete(photo);

        redisTemplate.delete("*" + photoId.toString());

        PhotoDto.DeleteResponse photoDeleteResponse = PhotoDto.DeleteResponse.builder()
                .photoId(photo.getPhotoId().toString())
                .build();

        return photoDeleteResponse;
    }
}
