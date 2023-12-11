package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.service.GeoCodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/core/external-api")
@RequiredArgsConstructor
public class ExternalApiController {

    private final GeoCodingService geoCodingService;

    @GetMapping("/reverse-geo-coding")
    public ResponseEntity<String> reverseGeoCoding(@RequestParam Double latitude, @RequestParam Double longitude) {

        final String addrFromCoordinate = geoCodingService.getAddressFromCoordinate(latitude, longitude);

        return ResponseEntity.status(HttpStatus.OK).body(addrFromCoordinate);
    }

    @GetMapping("/geo-coding")
    public ResponseEntity<String> geoCoding(@RequestParam String address) {

        final String coordinateFromAddress = geoCodingService.getCoordinateFromAddress(address);

        return ResponseEntity.status(HttpStatus.OK).body(coordinateFromAddress);
    }
}
