package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.entity.*;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.exception.PhotoErrorResult;
import com.blackshoe.esthetecoreservice.exception.PhotoException;
import com.blackshoe.esthetecoreservice.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExhibitionServiceImpl implements ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final NewWorkRepository newWorkRepository;
    private final UserRepository userRepository;
    private final RedisTemplate redisTemplate;
    private final SupportRepository supportRepository;
    private final PhotoRepository photoRepository;
    private final ViewRepository viewRepository;

    @Override
    @Transactional
    public ExhibitionDto.CreateExhibitionResponse createExhibition(ExhibitionDto.CreateExhibitionRequest exhibitionCreateRequest) throws JsonProcessingException {

        final User photographer = userRepository.findByUserId(UUID.fromString(exhibitionCreateRequest.getUserId()))
                .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.USER_NOT_FOUND));

        final Exhibition exhibition = Exhibition.builder()
                .title(exhibitionCreateRequest.getTitle())
                .description(exhibitionCreateRequest.getDescription())
                .thumbnail(exhibitionCreateRequest.getThumbnail())
                .build();

        exhibition.setUser(photographer);

        final Exhibition savedExhibition = exhibitionRepository.save(exhibition);

        saveOrUpdateNewWork(photographer.getUserId(), savedExhibition);

        final ExhibitionDto.CreateExhibitionResponse exhibitionCreateExhibitionResponse = ExhibitionDto.CreateExhibitionResponse.builder()
                .exhibitionId(savedExhibition.getExhibitionId().toString())
                .createdAt(savedExhibition.getCreatedAt().toString())
                .build();

        return exhibitionCreateExhibitionResponse;
    }

    @Override
    @Transactional
    public ExhibitionDto.DeleteExhibitionResponse deleteExhibition(UUID exhibitionId) {

        final Exhibition exhibition = exhibitionRepository.findByExhibitionId(exhibitionId)
                .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.EXHIBITION_NOT_FOUND));

        exhibitionRepository.delete(exhibition);

        newWorkRepository.deleteByExhibition(exhibition);

        redisTemplate.delete("photographer_" + exhibition.getUser().getUserId() + "_exhibition_" + exhibition.getExhibitionId().toString());

        final ExhibitionDto.DeleteExhibitionResponse exhibitionDeleteExhibitionResponse = ExhibitionDto.DeleteExhibitionResponse.builder()
                .exhibitionId(exhibition.getExhibitionId().toString())
                .deletedAt(LocalDateTime.now().toString())
                .build();

        return exhibitionDeleteExhibitionResponse;
    }

    @Override
    public ExhibitionDto.ReadRandomExhibitionResponse readRandomExhibition() {

        long exhibitionCount = exhibitionRepository.countAllBy();

        if (exhibitionCount == 0) {
            throw new ExhibitionException(ExhibitionErrorResult.EXHIBITION_NOT_FOUND);
        }

        final Long exhibitionId = (long) (Math.random() * exhibitionCount);
        Exhibition exhibition = exhibitionRepository.findById(exhibitionId).get();

        //photo cloudfronturl from exhibition.thumbnail(photoid)
        Photo thumbnailPhoto = photoRepository.findByPhotoId(UUID.fromString(exhibition.getThumbnail())).orElseThrow(() -> new PhotoException(PhotoErrorResult.PHOTO_NOT_FOUND));

        final ExhibitionDto.ReadRandomExhibitionResponse exhibitionReadRandomExhibitionResponse = ExhibitionDto.ReadRandomExhibitionResponse.builder()
                .exhibitionId(exhibition.getExhibitionId().toString())
                .title(exhibition.getTitle())
                .description(exhibition.getDescription())
                .thumbnail(thumbnailPhoto.getPhotoUrl().getCloudfrontUrl())
                .userId(exhibition.getUser().getUserId().toString())
                .nickname(exhibition.getUser().getNickname())
                .profileImg(exhibition.getUser().getProfileImgUrl().getCloudfrontUrl())
                .build();

        return exhibitionReadRandomExhibitionResponse;
    }

    @Override
    @Transactional
    public ExhibitionDto.UpdateViewOfExhibitionResponse viewExhibition(UUID exhibitionId, UUID userId) {
        ExhibitionDto.UpdateViewOfExhibitionResponse exhibitionUpdateViewOfExhibitionResponse = ExhibitionDto.UpdateViewOfExhibitionResponse.builder()
                .exhibitionId(exhibitionId.toString())
                .updatedAt(LocalDateTime.now().toString())
                .build();

        if (redisTemplate.opsForValue().get("view_exhibition_" + exhibitionId + "_user_" + userId) == null) {

            String redisKey = "view_exhibition_" + exhibitionId + "_user_" + userId;
            redisTemplate.opsForValue().set(redisKey, "0");
            redisTemplate.expire(redisKey, 60 * 60 * 24, java.util.concurrent.TimeUnit.SECONDS);

            Exhibition exhibition = exhibitionRepository.findByExhibitionId(exhibitionId).orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.EXHIBITION_NOT_FOUND));
            User user = userRepository.findByUserId(userId).orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.USER_NOT_FOUND));

            View view = View.builder()
                    .exhibition(exhibition)
                    .user(user)
                    .build();

            viewRepository.save(view);

            exhibition.increaseViewCount();
        }

        return exhibitionUpdateViewOfExhibitionResponse;
    }

    @Transactional
    public void saveOrUpdateNewWork(UUID photographerId, Exhibition exhibition) throws JsonProcessingException {
        List<Support> supports = supportRepository.findAllByPhotographerId(photographerId);

        String[] userIdWithCondition;
        List<String[]> supporters = new ArrayList<>();

        for (Support support : supports) {
            userIdWithCondition = new String[]{support.getUser().getUserId().toString(), "true"};

            supporters.add(userIdWithCondition);
        }

        String hasNewRedisKey = "photographer_" + photographerId + "_exhibition_" + exhibition.getExhibitionId().toString();

        ObjectMapper objectMapper = new ObjectMapper();

        String supportersJson = objectMapper.writeValueAsString(supporters);

        try {
            supportersJson = objectMapper.writeValueAsString(supporters);
        } catch (JsonProcessingException e) {
            log.error("JSON 변환 실패: {}", e.getMessage());
        }

        redisTemplate.opsForValue().set(hasNewRedisKey, supportersJson);
        redisTemplate.expire(hasNewRedisKey, 60 * 60 * 24 * 7, java.util.concurrent.TimeUnit.SECONDS);

        User photographer = userRepository.findByUserId(photographerId)
                .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.USER_NOT_FOUND));

        NewWork newWork = NewWork.builder()
                .photographerId(photographerId)
                .exhibitionId(exhibition.getExhibitionId())
                .build();

        newWork.setPhotographer(photographer);
        newWork.setExhibition(exhibition);

        newWorkRepository.save(newWork);
    }


}
