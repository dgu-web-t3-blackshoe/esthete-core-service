package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.NewWorkDto;
import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service @RequiredArgsConstructor
public class NewWorkServiceImpl implements NewWorkService{
    private final NewWorkRepository newWorkRepository;
    private final RedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final SupportRepository supportRepository;
    private final PhotoRepository photoRepository;
    private final ExhibitionRepository exhibitionRepository;

    @Override
    public List<NewWorkDto.ReadNewWorkResponse> readNewWork(UUID userId) {

        List<NewWorkDto.ReadNewWorkResponse> newWorkReadResponsNewWorks = new ArrayList<>();
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        boolean hasNewPhoto = false;
        boolean hasNewExhibition = false;

        String hasNewRedisKey = userId.toString() + "*";
        Set<String> keys = redisTemplate.keys(hasNewRedisKey);

        for (String key : keys) {
            String[] splitKey = key.split("_");
            NewWorkDto.ReadNewWorkResponse newWorkReadNewWorkResponse = null;

            if(splitKey[2] == "photo") {
                hasNewPhoto = "true".equals(redisTemplate.opsForValue().get(key));
                newWorkReadNewWorkResponse = NewWorkDto.ReadNewWorkResponse.builder()
                        .photoId(splitKey[3])
                        .hasNewPhoto(hasNewPhoto)
                        .build();
            }
            else {
                hasNewExhibition = "true".equals(redisTemplate.opsForValue().get(key));
                newWorkReadNewWorkResponse = NewWorkDto.ReadNewWorkResponse.builder()
                        .exhibitionId(splitKey[3])
                        .hasNewExhibition(hasNewExhibition)
                        .build();
            }
            newWorkReadNewWorkResponse.setProfileImg(user.getProfileImgUrl().getCloudfrontUrl());
            newWorkReadNewWorkResponse.setNickname(user.getNickname());
            newWorkReadNewWorkResponse.setPhotographerId(userId.toString());

            newWorkReadResponsNewWorks.add(newWorkReadNewWorkResponse);
        }

        return newWorkReadResponsNewWorks;
    }

    @Override
    public NewWorkDto.UpdateNewWorkResponse viewNewPhoto(NewWorkDto.UpdateViewOfPhotoRequest updateRequest) {

        String userId = updateRequest.getUserId();
        String photoId = updateRequest.getPhotoId();

        Optional<Photo> photo = photoRepository.findByPhotoId(UUID.fromString(photoId));

        User photographer = photo.get().getUser();

        redisTemplate.opsForValue().get("*photo_" + photoId);
        List<String[]> supporters = (List<String[]>) redisTemplate.opsForValue().get("*photo_" + photoId);

        for(String[] supporter : supporters){
            if(supporter[0].equals(userId)){
                supporter[1] = "false";
            }
        }

        NewWorkDto.UpdateNewWorkResponse newWorkUpdateNewWorkResponse = NewWorkDto.UpdateNewWorkResponse.builder()
                .updatedAt(LocalDateTime.now().toString())
                .build();
        return newWorkUpdateNewWorkResponse;
    }

    @Override
    public NewWorkDto.UpdateNewWorkResponse viewNewExhibition(NewWorkDto.UpdateViewOfExhibitionRequest updateRequest) {

            String userId = updateRequest.getUserId();
            String exhibitionId = updateRequest.getExhibitionId();

            Optional<Photo> photo = photoRepository.findByPhotoId(UUID.fromString(exhibitionId));

            User photographer = photo.get().getUser();

            redisTemplate.opsForValue().get("*exhibition_" + exhibitionId);
            List<String[]> supporters = (List<String[]>) redisTemplate.opsForValue().get("*exhibition_" + exhibitionId);

            for(String[] supporter : supporters){
                if(supporter[0].equals(userId)){
                    supporter[1] = "false";
                }
            }

            NewWorkDto.UpdateNewWorkResponse newWorkUpdateNewWorkResponse = NewWorkDto.UpdateNewWorkResponse.builder()
                    .updatedAt(LocalDateTime.now().toString())
                    .build();
            return newWorkUpdateNewWorkResponse;
    }
}
