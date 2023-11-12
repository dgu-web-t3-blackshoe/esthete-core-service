package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.repository.ExhibitionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExhibitionServiceImpl implements ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;

    @Override
    @Transactional
    public ExhibitionDto.CreateResponse createExhibition(ExhibitionDto.CreateRequest exhibitionCreateRequest) {

        final Exhibition exhibition = Exhibition.builder()
                .title(exhibitionCreateRequest.getTitle())
                .description(exhibitionCreateRequest.getDescription())
                .thumbnail(exhibitionCreateRequest.getThumbnail())
                .build();

        final Exhibition savedExhibition = exhibitionRepository.save(exhibition);

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

        final ExhibitionDto.DeleteResponse exhibitionDeleteResponse = ExhibitionDto.DeleteResponse.builder()
                .exhibitionId(exhibition.getExhibitionId().toString())
                .deletedAt(LocalDateTime.now().toString())
                .build();

        return exhibitionDeleteResponse;
    }

    @Override
    public ExhibitionDto.GetRandomResponse getRandomExhibition() {

            Optional<Exhibition> optionalExhibition = Optional.empty();

            while (optionalExhibition.isEmpty()) {
                final Long exhibitionId = (long) (Math.random() * 1000000);
                optionalExhibition = exhibitionRepository.findById(exhibitionId);
            }

            final Exhibition exhibition = optionalExhibition.get();

            final ExhibitionDto.GetRandomResponse exhibitionGetRandomResponse = ExhibitionDto.GetRandomResponse.builder()
                    .exhibitionId(exhibition.getExhibitionId().toString())
                    .title(exhibition.getTitle())
                    .description(exhibition.getDescription())
                    .thumbnail(exhibition.getThumbnail())
                    .userId(exhibition.getUser().getUserId().toString())
                    .nickname(exhibition.getUser().getNickname())
                    .profileImg(exhibition.getUser().getProfileImgUrl().getCloudfrontUrl())
                    .build();

            return exhibitionGetRandomResponse;
    }
}
