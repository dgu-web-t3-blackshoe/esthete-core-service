package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;

import java.util.UUID;

public interface ExhibitionService {
    ExhibitionDto.CreateResponse createExhibition(ExhibitionDto.CreateRequest exhibitionCreateRequest);

    ExhibitionDto.DeleteResponse deleteExhibition(UUID exhibitionId);
}
