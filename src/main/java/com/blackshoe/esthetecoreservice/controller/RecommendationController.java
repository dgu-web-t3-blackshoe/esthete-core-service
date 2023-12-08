package com.blackshoe.esthetecoreservice.controller;


import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
import com.blackshoe.esthetecoreservice.service.ExhibitionService;
import com.blackshoe.esthetecoreservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController @Slf4j
@RequestMapping("/core/recommendations") @RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final ExhibitionService exhibitionService;
    private final UserRepository userRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<ExhibitionDto.ReadRecommendedExhibitionResponse> getRecommendedExhibitions(@PathVariable UUID userId) throws Exception {

        ExhibitionDto.ReadRecommendedExhibitionResponse readRecommendedExhibitionResponse;

        ExhibitionDto.ReadRandomExhibitionResponse readRandomExhibitionResponse;

        //user 100명 이하일 시 return
        if(userRepository.countAllBy() < 100) {
            log.info("user 100명 이하");
            log.info("user count: {}", userRepository.count());
            readRandomExhibitionResponse = exhibitionService.readRandomExhibition();

            readRecommendedExhibitionResponse = convertRandomResToRecommendRes(readRandomExhibitionResponse);

            return ResponseEntity.status(HttpStatus.OK).body(readRecommendedExhibitionResponse);
        }

        readRecommendedExhibitionResponse = recommendationService.getRecommendedExhibitions(userId);

        if(readRecommendedExhibitionResponse == null) {
            readRandomExhibitionResponse = exhibitionService.readRandomExhibition();
            readRecommendedExhibitionResponse = convertRandomResToRecommendRes(readRandomExhibitionResponse);
        }

        return ResponseEntity.status(HttpStatus.OK).body(readRecommendedExhibitionResponse);
    }

    private ExhibitionDto.ReadRecommendedExhibitionResponse convertRandomResToRecommendRes(ExhibitionDto.ReadRandomExhibitionResponse readRandomExhibitionResponse) {
        ExhibitionDto.ReadRecommendedExhibitionResponse readRecommendedExhibitionResponse = ExhibitionDto.ReadRecommendedExhibitionResponse.builder()
                .exhibitionId(readRandomExhibitionResponse.getExhibitionId())
                .title(readRandomExhibitionResponse.getTitle())
                .description(readRandomExhibitionResponse.getDescription())
                .thumbnail(readRandomExhibitionResponse.getThumbnail())
                .userId(readRandomExhibitionResponse.getUserId())
                .nickname(readRandomExhibitionResponse.getNickname())
                .profileImg(readRandomExhibitionResponse.getProfileImg())
                .build();

        return readRecommendedExhibitionResponse;
    }
}
