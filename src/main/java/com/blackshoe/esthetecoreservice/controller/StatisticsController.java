package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.ExhibitionCountDto;
import com.blackshoe.esthetecoreservice.dto.UserCountDto;
import com.blackshoe.esthetecoreservice.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/core/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/exhibitions/count")
    public ResponseEntity<ExhibitionCountDto> getExhibitionCount(@RequestParam int interval, @RequestParam String now) {

        LocalDateTime localDateTimeNow = LocalDateTime.parse(now);

        ExhibitionCountDto exhibitionCountDto = statisticsService.getExhibitionCount(interval, localDateTimeNow);

        return ResponseEntity.status(HttpStatus.OK).body(exhibitionCountDto);
    }

    @GetMapping("/users/count")
    public ResponseEntity<UserCountDto> getUserCount(@RequestParam int interval, @RequestParam String now) {

        LocalDateTime localDateTimeNow = LocalDateTime.parse(now);

        UserCountDto userCountDto = statisticsService.getUserCount(interval, localDateTimeNow);

        return ResponseEntity.status(HttpStatus.OK).body(userCountDto);
    }
}
