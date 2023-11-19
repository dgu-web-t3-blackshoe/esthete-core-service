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

@Slf4j
@RequiredArgsConstructor
@Service
public class PhotoServiceImpl implements PhotoService{

    private final AmazonS3Client amazonS3Client;
    private final PhotoRepository photoRepository;

    //redis
    private final RedisTemplate redisTemplate;

    private final NewWorkRepository newWorkRepository;
    private final UserRepository userRepository;
    private final SupportRepository supportRepository;
    private final GenreRepository genreRepository;

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
    public PhotoDto uploadPhotoToS3(MultipartFile photo, PhotoDto.PhotoUploadRequest photoUploadRequest) {
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

        Photo uploadedPhoto = Photo.builder()
                .photoId(photoId)
                .photoUrl(uploadedPhotoUrl)
                .title(photoUploadRequest.getTitle())
                .description(photoUploadRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .photoGenres(photoGenres)
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

        PhotoDto photoDto = PhotoDto.builder()
                .photoId(photoId)
                .photoUrl(uploadedPhotoUrl)
                .title(photoUploadRequest.getTitle())
                .description(photoUploadRequest.getDescription())
                .detail(photoUploadRequest.getDetail())
                .isPublic(Boolean.valueOf(photoUploadRequest.getIsPublic()))
                .createdAt(uploadedPhoto.getCreatedAt())
                .build();

        return photoDto;
    }

    @Override
    @Transactional
    public PhotoDto.GetPhotoUrlResponse getPhotoUrl(UUID photoId) {
        Photo photo = photoRepository.findByPhotoId(photoId).orElseThrow(() -> new PhotoException(PhotoErrorResult.PHOTO_NOT_FOUND));

        PhotoUrl photoUrl = photo.getPhotoUrl();

        PhotoDto.GetPhotoUrlResponse getPhotoUrlResponse = PhotoDto.GetPhotoUrlResponse.builder()
                .title(photo.getTitle())
                .description(photo.getDescription())
                .cloudfrontUrl(photoUrl.getCloudfrontUrl())
                .build();

        return getPhotoUrlResponse;
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
