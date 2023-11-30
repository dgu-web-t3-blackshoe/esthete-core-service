package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.NewWorkDto;
import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service @RequiredArgsConstructor @Slf4j
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

        ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱을 위한 ObjectMapper

        String hasNewRedisKey = "photographer_" + userId.toString() + "*";
        Set<String> keys = redisTemplate.keys(hasNewRedisKey);

        for (String key : keys) {
            try {
                String jsonValue = (String) redisTemplate.opsForValue().get(key); // Object를 String으로 캐스팅
                List<List<String>> values = objectMapper.readValue(jsonValue, new TypeReference<List<List<String>>>() {});

                boolean hasNew = values.stream().anyMatch(pair -> "true".equals(pair.get(1)));

                String[] splitKey = key.split("_");
                NewWorkDto.ReadNewWorkResponse newWorkReadNewWorkResponse = null;

                if ("photo".equals(splitKey[2])) {
                    newWorkReadNewWorkResponse = NewWorkDto.ReadNewWorkResponse.builder()
                            .photoId(splitKey[3])
                            .hasNewPhoto(hasNew)
                            .build();
                } else {
                    newWorkReadNewWorkResponse = NewWorkDto.ReadNewWorkResponse.builder()
                            .exhibitionId(splitKey[3])
                            .hasNewExhibition(hasNew)
                            .build();
                }

                newWorkReadNewWorkResponse.setUpdatedAt(LocalDateTime.now().toString());
                newWorkReadNewWorkResponse.setProfileImg(user.getProfileImgUrl().getCloudfrontUrl());
                newWorkReadNewWorkResponse.setNickname(user.getNickname());
                newWorkReadNewWorkResponse.setPhotographerId(userId.toString());

                newWorkReadResponsNewWorks.add(newWorkReadNewWorkResponse);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                log.info("JSON 파싱에 실패했습니다.");
            }
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
