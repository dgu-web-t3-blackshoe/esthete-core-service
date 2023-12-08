package com.blackshoe.esthetecoreservice.controller;


import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.service.ExhibitionService;
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
    private final ExhibitionService exhibitionService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<ExhibitionDto.ReadRecommendedExhibitionResponse>> getRecommendedExhibitions(@PathVariable UUID userId) throws Exception {

        List<ExhibitionDto.ReadRecommendedExhibitionResponse> readRecommendedExhibitionResponses;

        readRecommendedExhibitionResponses = recommendationService.getRecommendedExhibitions(userId);

        if(readRecommendedExhibitionResponses.size() == 0) {
            ExhibitionDto.ReadRandomExhibitionResponse readRandomExhibitionResponse = exhibitionService.readRandomExhibition();

            ExhibitionDto.ReadRecommendedExhibitionResponse readRecommendedExhibitionResponse = ExhibitionDto.ReadRecommendedExhibitionResponse.builder()
                    .exhibitionId(readRandomExhibitionResponse.getExhibitionId())
                    .title(readRandomExhibitionResponse.getTitle())
                    .description(readRandomExhibitionResponse.getDescription())
                    .thumbnail(readRandomExhibitionResponse.getThumbnail())
                    .userId(readRandomExhibitionResponse.getUserId())
                    .nickname(readRandomExhibitionResponse.getNickname())
                    .profileImg(readRandomExhibitionResponse.getProfileImg())
                    .build();

            readRecommendedExhibitionResponses.add(readRecommendedExhibitionResponse);
        }

        return ResponseEntity.status(HttpStatus.OK).body(readRecommendedExhibitionResponses);
    }
}
