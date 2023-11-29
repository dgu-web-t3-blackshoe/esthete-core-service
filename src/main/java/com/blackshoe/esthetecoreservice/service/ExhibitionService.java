package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;

import java.util.UUID;

public interface ExhibitionService {
    ExhibitionDto.CreateExhibitionResponse createExhibition(ExhibitionDto.CreateExhibitionRequest exhibitionCreateRequest);

    ExhibitionDto.DeleteExhibitionResponse deleteExhibition(UUID exhibitionId);

    ExhibitionDto.ReadRandomExhibitionResponse readRandomExhibition();
}
