package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.NewWorkDto;
import com.blackshoe.esthetecoreservice.entity.NewWork;
import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.Support;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List<NewWorkDto.ReadResponse> readNewWork(UUID userId) {

        List<NewWorkDto.ReadResponse> newWorkReadResponses = new ArrayList<>();
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        boolean hasNewPhoto = false;
        boolean hasNewExhibition = false;

        String hasNewRedisKey = userId.toString() + "*";
        Set<String> keys = redisTemplate.keys(hasNewRedisKey);

        for (String key : keys) {
            String[] splitKey = key.split("_");
            NewWorkDto.ReadResponse newWorkReadResponse = null;

            if(splitKey[2] == "photo") {
                hasNewPhoto = "true".equals(redisTemplate.opsForValue().get(key));
                newWorkReadResponse = NewWorkDto.ReadResponse.builder()
                        .photoId(splitKey[3])
                        .hasNewPhoto(hasNewPhoto)
                        .build();
            }
            else {
                hasNewExhibition = "true".equals(redisTemplate.opsForValue().get(key));
                newWorkReadResponse = NewWorkDto.ReadResponse.builder()
                        .exhibitionId(splitKey[3])
                        .hasNewExhibition(hasNewExhibition)
                        .build();
            }
            newWorkReadResponse.setProfileImg(user.getProfileImgUrl().getCloudfrontUrl());
            newWorkReadResponse.setNickname(user.getNickname());
            newWorkReadResponse.setPhotographerId(userId.toString());

            newWorkReadResponses.add(newWorkReadResponse);
        }

        return newWorkReadResponses;
    }

    @Override
    public NewWorkDto.UpdateResponse viewNewPhoto(NewWorkDto.UpdateViewOfPhotoRequest updateRequest) {

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

        NewWorkDto.UpdateResponse newWorkUpdateResponse = NewWorkDto.UpdateResponse.builder()
                .updatedAt(LocalDateTime.now().toString())
                .build();
        return newWorkUpdateResponse;
    }

    @Override
    public NewWorkDto.UpdateResponse viewNewExhibition(NewWorkDto.UpdateViewOfExhibitionRequest updateRequest) {

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

            NewWorkDto.UpdateResponse newWorkUpdateResponse = NewWorkDto.UpdateResponse.builder()
                    .updatedAt(LocalDateTime.now().toString())
                    .build();
            return newWorkUpdateResponse;
    }
}
