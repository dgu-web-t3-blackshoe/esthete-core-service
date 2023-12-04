package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.UUID;

public interface ExhibitionService {
    ExhibitionDto.CreateExhibitionResponse createExhibition(ExhibitionDto.CreateExhibitionRequest exhibitionCreateRequest) throws JsonProcessingException;

    ExhibitionDto.DeleteExhibitionResponse deleteExhibition(UUID exhibitionId);

    ExhibitionDto.ReadRandomExhibitionResponse readRandomExhibition();
}
