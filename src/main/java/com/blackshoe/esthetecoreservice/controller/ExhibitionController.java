package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.dto.ResponseDto;
import com.blackshoe.esthetecoreservice.service.ExhibitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}
