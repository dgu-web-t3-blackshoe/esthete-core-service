package com.blackshoe.esthetecoreservice.controller;


import com.blackshoe.esthetecoreservice.dto.NewWorkDto;
import com.blackshoe.esthetecoreservice.service.NewWorkService;
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
    public ResponseEntity<List<NewWorkDto.ReadResponse>> readNewWork(@PathVariable UUID userId) {
        List<NewWorkDto.ReadResponse> newWorkReadResponses = newWorkService.readNewWork(userId);

        return ResponseEntity.status(HttpStatus.OK).body(newWorkReadResponses);
    }

    @PostMapping("/{userId}/photos/{photoId}")
    public ResponseEntity<NewWorkDto.UpdateResponse> viewNewPhoto(@PathVariable UUID userId, @PathVariable UUID photoId) {

        NewWorkDto.UpdateViewOfPhotoRequest updateRequest = NewWorkDto.UpdateViewOfPhotoRequest.builder()
                .userId(String.valueOf(userId))
                .photoId(String.valueOf(photoId))
                .build();

        NewWorkDto.UpdateResponse newWorkUpdateResponse = newWorkService.viewNewPhoto(updateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(newWorkUpdateResponse);
    }

    @PostMapping("/{userId}/exhibitions/{exhibitionId}")
    public ResponseEntity<NewWorkDto.UpdateResponse> viewNewExhibition(@PathVariable UUID userId, @PathVariable UUID exhibitionId) {

        NewWorkDto.UpdateViewOfExhibitionRequest updateRequest = NewWorkDto.UpdateViewOfExhibitionRequest.builder()
                .userId(String.valueOf(userId))
                .exhibitionId(String.valueOf(exhibitionId))
                .build();

        NewWorkDto.UpdateResponse newWorkUpdateResponse = newWorkService.viewNewExhibition(updateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(newWorkUpdateResponse);
    }
}
