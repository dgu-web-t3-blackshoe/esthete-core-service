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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
    public ExhibitionDto.CreateResponse createExhibition(ExhibitionDto.CreateRequest exhibitionCreateRequest) {

        final User photographer = userRepository.findByUserId(UUID.fromString(exhibitionCreateRequest.getUserId()))
                .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.USER_NOT_FOUND));

        final Exhibition exhibition = Exhibition.builder()
                .title(exhibitionCreateRequest.getTitle())
                .description(exhibitionCreateRequest.getDescription())
                .thumbnail(exhibitionCreateRequest.getThumbnail())
                .build();

        exhibition.setUser(photographer);

        final Exhibition savedExhibition = exhibitionRepository.save(exhibition);


        NewWork newWork = newWorkRepository.findByPhotographerId(photographer.getUserId());
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
                    .build();
        }
        else {
            newWork.setExhibition(savedExhibition);
        }

        newWorkRepository.save(newWork);

        final ExhibitionDto.CreateResponse exhibitionCreateResponse = ExhibitionDto.CreateResponse.builder()
                .exhibitionId(savedExhibition.getExhibitionId().toString())
                .createdAt(savedExhibition.getCreatedAt().toString())
                .build();

        return exhibitionCreateResponse;
    }

    @Override
    @Transactional
    public ExhibitionDto.DeleteResponse deleteExhibition(UUID exhibitionId) {

        final Exhibition exhibition = exhibitionRepository.findByExhibitionId(exhibitionId)
                .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.EXHIBITION_NOT_FOUND));

        exhibitionRepository.delete(exhibition);

        redisTemplate.delete("*" + exhibitionId.toString());

        final ExhibitionDto.DeleteResponse exhibitionDeleteResponse = ExhibitionDto.DeleteResponse.builder()
                .exhibitionId(exhibition.getExhibitionId().toString())
                .deletedAt(LocalDateTime.now().toString())
                .build();

        return exhibitionDeleteResponse;
    }

    @Override
    public ExhibitionDto.ReadRandomResponse readRandomExhibition() {

            Optional<Exhibition> optionalExhibition = Optional.empty();

            while (optionalExhibition.isEmpty()) {
                final Long exhibitionId = (long) (Math.random() * 1000000);
                optionalExhibition = exhibitionRepository.findById(exhibitionId);
            }

            final Exhibition exhibition = optionalExhibition.get();

            final ExhibitionDto.ReadRandomResponse exhibitionReadRandomResponse = ExhibitionDto.ReadRandomResponse.builder()
                    .exhibitionId(exhibition.getExhibitionId().toString())
                    .title(exhibition.getTitle())
                    .description(exhibition.getDescription())
                    .thumbnail(exhibition.getThumbnail())
                    .userId(exhibition.getUser().getUserId().toString())
                    .nickname(exhibition.getUser().getNickname())
                    .profileImg(exhibition.getUser().getProfileImgUrl().getCloudfrontUrl())
                    .build();

            return exhibitionReadRandomResponse;
    }
}
