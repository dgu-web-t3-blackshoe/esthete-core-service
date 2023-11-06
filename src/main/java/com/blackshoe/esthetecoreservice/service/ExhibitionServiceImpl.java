package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.repository.ExhibitionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
                .exhibitionId(savedExhibition.getExhibitionId())
                .createdAt(savedExhibition.getCreatedAt())
                .build();

        return exhibitionCreateResponse;
    }
}
