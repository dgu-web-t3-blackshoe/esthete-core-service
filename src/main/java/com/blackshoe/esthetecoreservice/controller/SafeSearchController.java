package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.service.SafeSearchFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/core/safe-search")
@RequiredArgsConstructor
public class SafeSearchController {

    private final SafeSearchFilterService safeSearchFilterService;

    @PostMapping
    public ResponseEntity checkSafeSearch(@RequestPart MultipartFile request) {

        safeSearchFilterService.safeSearchFilter(request);

        return ResponseEntity.ok().build();
    }
}
