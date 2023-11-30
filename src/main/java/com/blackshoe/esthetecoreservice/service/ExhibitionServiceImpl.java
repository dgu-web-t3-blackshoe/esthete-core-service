package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.NewWork;
import com.blackshoe.esthetecoreservice.entity.Support;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.repository.ExhibitionRepository;
import com.blackshoe.esthetecoreservice.repository.NewWorkRepository;
import com.blackshoe.esthetecoreservice.repository.SupportRepository;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
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
    @Override
    @Transactional
    public ExhibitionDto.CreateExhibitionResponse createExhibition(ExhibitionDto.CreateExhibitionRequest exhibitionCreateRequest) {

        final User photographer = userRepository.findByUserId(UUID.fromString(exhibitionCreateRequest.getUserId()))
                .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.USER_NOT_FOUND));

        final Exhibition exhibition = Exhibition.builder()
                .title(exhibitionCreateRequest.getTitle())
                .description(exhibitionCreateRequest.getDescription())
                .thumbnail(exhibitionCreateRequest.getThumbnail())
                .build();

        exhibition.setUser(photographer);

        final Exhibition savedExhibition = exhibitionRepository.save(exhibition);


        NewWork newWork = newWorkRepository.findByPhotographerId(photographer.getUserId()).orElse(null);

        List<Support> supports = supportRepository.findAllByPhotographerId(photographer.getUserId());

        String[] userIdWithCondition;
        List<String[]> supporters = new ArrayList<>();

        for(Support support : supports){
            userIdWithCondition = new String[]{support.getUser().getUserId().toString(), "true"};
            supporters.add(userIdWithCondition);
        }

        String hasNewRedisKey = "photographer_" + exhibitionCreateRequest.getUserId().toString() + "_exhibition_" + savedExhibition.getExhibitionId().toString();
        redisTemplate.opsForValue().set(hasNewRedisKey, supporters.toString());
        redisTemplate.expire(hasNewRedisKey, 60 * 60 * 24, java.util.concurrent.TimeUnit.SECONDS);

        if(newWork == null){
            newWork = NewWork.builder()
                    .exhibition(savedExhibition)
                    .photographer(photographer)
                    .photographerId(UUID.fromString(exhibitionCreateRequest.getUserId()))
                    .exhibitionId(savedExhibition.getExhibitionId())
                    .build();
        }
        else {
            newWork.setExhibition(savedExhibition);
        }

        newWorkRepository.save(newWork);

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

        redisTemplate.delete("*" + exhibitionId.toString());

        final ExhibitionDto.DeleteExhibitionResponse exhibitionDeleteExhibitionResponse = ExhibitionDto.DeleteExhibitionResponse.builder()
                .exhibitionId(exhibition.getExhibitionId().toString())
                .deletedAt(LocalDateTime.now().toString())
                .build();

        return exhibitionDeleteExhibitionResponse;
    }

    @Override
    public ExhibitionDto.ReadRandomExhibitionResponse readRandomExhibition() {

            Optional<Exhibition> optionalExhibition = Optional.empty();

            while (optionalExhibition.isEmpty()) {
                final Long exhibitionId = (long) (Math.random() * 1000000);
                optionalExhibition = exhibitionRepository.findById(exhibitionId);
            }

            final Exhibition exhibition = optionalExhibition.get();

            final ExhibitionDto.ReadRandomExhibitionResponse exhibitionReadRandomExhibitionResponse = ExhibitionDto.ReadRandomExhibitionResponse.builder()
                    .exhibitionId(exhibition.getExhibitionId().toString())
                    .title(exhibition.getTitle())
                    .description(exhibition.getDescription())
                    .thumbnail(exhibition.getThumbnail())
                    .userId(exhibition.getUser().getUserId().toString())
                    .nickname(exhibition.getUser().getNickname())
                    .profileImg(exhibition.getUser().getProfileImgUrl().getCloudfrontUrl())
                    .build();

            return exhibitionReadRandomExhibitionResponse;
    }
}
