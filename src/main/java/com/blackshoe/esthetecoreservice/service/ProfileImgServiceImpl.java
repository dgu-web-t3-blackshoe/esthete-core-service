package com.blackshoe.esthetecoreservice.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

import com.blackshoe.esthetecoreservice.dto.ProfileImgUrlDto;
import com.blackshoe.esthetecoreservice.entity.ProfileImgUrl;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.repository.ProfileImgUrlRepository;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.UUID;


@Slf4j
@Service @RequiredArgsConstructor
public class ProfileImgServiceImpl implements ProfileImgService {
    private final AmazonS3Client amazonS3Client;
    private final UserRepository userRepository;

    private final ProfileImgUrlRepository profileImgUrlRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET;
    @Value("${cloud.aws.cloudfront.distribution-domain}")
    private String DISTRIBUTION_DOMAIN;
    @Value("${cloud.aws.s3.root-directory}")
    private String ROOT_DIRECTORY;
    //@Value("${cloud.aws.s3.profile-directory}")
    @Value("profile")
    private String PROFILEIMG_DIRECTORY;

    @Override
    @Transactional
    public ProfileImgUrlDto uploadProfileImg(UUID userId, MultipartFile profileImg) {

        String s3FilePath = userId + "/" + PROFILEIMG_DIRECTORY;

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        ProfileImgUrlDto profileImgUrlDto;

        if(profileImg == null || profileImg.isEmpty()) {
            profileImgUrlDto = ProfileImgUrlDto.builder()
                    .cloudfrontUrl("")
                    .s3Url("")
                    .build();

            return profileImgUrlDto;
        }


        String fileExtension = profileImg.getOriginalFilename().substring(profileImg.getOriginalFilename().lastIndexOf("."));

        ProfileImgUrl profileImgUrl = null;
        UUID profileImgUrlId = UUID.randomUUID();

        String key = ROOT_DIRECTORY + "/" + s3FilePath + "/" + profileImgUrlId + fileExtension;

//        if (!ContentType.isContentTypeValid(profileImg.getContentType())) {
//            throw new UserException(UserErrorResult.INVALID_PROFILEIMG_TYPE);
//        }

        if (profileImg.getSize() > 52428800) {
            throw new UserException(UserErrorResult.INVALID_PROFILEIMG_SIZE);
        }

        try {
            amazonS3Client.putObject(BUCKET, key, profileImg.getInputStream(), null);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException(UserErrorResult.PROFILEIMG_UPLOAD_FAILED);
        }

        String s3Url;

        try {
            s3Url = amazonS3Client.getUrl(BUCKET, key).toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException(UserErrorResult.PROFILEIMG_UPLOAD_FAILED);
        }

        String cloudFrontUrl = DISTRIBUTION_DOMAIN + "/" + key;

        profileImgUrl = ProfileImgUrl.builder()
                .profileImgUrlId(profileImgUrlId)
                .s3Url(s3Url)
                .cloudfrontUrl(cloudFrontUrl)
                .build();

        user.setProfileImgUrl(profileImgUrl);

        profileImgUrlDto = ProfileImgUrlDto.builder()
                .s3Url(s3Url)
                .cloudfrontUrl(cloudFrontUrl)
                .build();

        return profileImgUrlDto;
    }

    @Override
    @Transactional
    public void deleteProfileImg(UUID userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));
        String profileImgS3Url = user.getProfileImgUrl().getS3Url();

        //@TODO: delete profileImgUrl from user
        //profileImgUrlRepository.deleteById(user.getProfileImgUrl().getId());

        if(profileImgS3Url.equals("")){
            return;
        }

        String key = profileImgS3Url.substring(profileImgS3Url.indexOf(ROOT_DIRECTORY));

        try {
            amazonS3Client.deleteObject(BUCKET, key);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException(UserErrorResult.PROFILEIMG_DELETE_FAILED);
        }


    }

    @Override
    @Transactional
    public ProfileImgUrlDto getUserPresentProfileImgUrlDto(UUID userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        ProfileImgUrl profileImgUrl = user.getProfileImgUrl();

        ProfileImgUrlDto profileImgUrlDto = ProfileImgUrlDto.builder()
                .cloudfrontUrl(profileImgUrl.getCloudfrontUrl())
                .s3Url(profileImgUrl.getS3Url())
                .build();

        return profileImgUrlDto;
    }

}
