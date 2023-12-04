package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;

import java.util.List;
import java.util.UUID;

public interface RecommendationService {
    void buildDataModel();
    List<ExhibitionDto.ReadRecommendedExhibitionResponse> getRecommendedExhibitions(UUID userId) throws Exception;
}
