package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.UUID;

public interface RecommendationService {
    //@Scheduled(cron = "0 0 6 * * *")
    //void buildDataModel();

    List<ExhibitionDto.ReadRecommendedExhibitionResponse> getRecommendedExhibitions(UUID userId) throws Exception;
}
