package com.blackshoe.esthetecoreservice.controller;


import com.blackshoe.esthetecoreservice.dto.NewWorkDto;
import com.blackshoe.esthetecoreservice.service.NewWorkService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/core/new-works")
@RequiredArgsConstructor
public class NewWorkController {
    private final NewWorkService newWorkService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<NewWorkDto.ReadNewWorkResponse>> readNewWork(@PathVariable UUID userId) {
        List<NewWorkDto.ReadNewWorkResponse> newWorkReadResponsNewWorks = newWorkService.readNewWork(userId);

        return ResponseEntity.status(HttpStatus.OK).body(newWorkReadResponsNewWorks);
    }


    @PostMapping("/{userId}/exhibitions/{exhibitionId}")
    public ResponseEntity<NewWorkDto.UpdateNewWorkResponse> viewNewExhibition(@PathVariable UUID userId, @PathVariable UUID exhibitionId) throws JsonProcessingException {

        NewWorkDto.UpdateViewOfExhibitionRequest updateRequest = NewWorkDto.UpdateViewOfExhibitionRequest.builder()
                .userId(String.valueOf(userId))
                .exhibitionId(String.valueOf(exhibitionId))
                .build();

        NewWorkDto.UpdateNewWorkResponse newWorkUpdateNewWorkResponse = newWorkService.viewNewExhibition(updateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(newWorkUpdateNewWorkResponse);
    }
}
