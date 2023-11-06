package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;

public interface ExhibitionService {
    ExhibitionDto.CreateResponse createExhibition(ExhibitionDto.CreateRequest exhibitionCreateRequest);
}
