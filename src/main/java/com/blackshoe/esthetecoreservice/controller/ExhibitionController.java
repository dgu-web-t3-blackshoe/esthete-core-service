package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.service.ExhibitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/exhibitions")
@RequiredArgsConstructor
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    @PostMapping
    public ResponseEntity<ExhibitionDto.CreateResponse> createExhibition(@RequestBody @Valid ExhibitionDto.CreateRequest exhibitionCreateRequest) {

        final ExhibitionDto.CreateResponse exhibitionCreateResponse = exhibitionService.createExhibition(exhibitionCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(exhibitionCreateResponse);
    }

    @DeleteMapping("/{exhibitionId}")
    public ResponseEntity<ExhibitionDto.DeleteResponse> deleteExhibition(@PathVariable UUID exhibitionId) {

        final ExhibitionDto.DeleteResponse exhibitionDeleteResponse = exhibitionService.deleteExhibition(exhibitionId);

        return ResponseEntity.status(HttpStatus.OK).body(exhibitionDeleteResponse);
    }
}