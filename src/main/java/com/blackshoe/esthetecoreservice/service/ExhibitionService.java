package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.entity.User;

import java.util.UUID;

public interface ExhibitionService {
    ExhibitionDto.CreateResponse createExhibition(ExhibitionDto.CreateRequest exhibitionCreateRequest);

    ExhibitionDto.DeleteResponse deleteExhibition(UUID exhibitionId);

    ExhibitionDto.ReadRandomResponse readRandomExhibition();
}
