package com.blackshoe.esthetecoreservice.controller;


import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/core/recommendations") @RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<ExhibitionDto.ReadRecommendedExhibitionResponse>> getRecommendedExhibitions(@PathVariable UUID userId) throws Exception {

        List<ExhibitionDto.ReadRecommendedExhibitionResponse> readRecommendedExhibitionResponses = recommendationService.getRecommendedExhibitions(userId);

        return ResponseEntity.status(HttpStatus.OK).body(readRecommendedExhibitionResponses);
    }

    @PostMapping("/build-data-model")
    public void buildDataModel() {
        recommendationService.buildDataModel();
    }

}
