package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionCountDto;
import com.blackshoe.esthetecoreservice.dto.UserCountDto;

import java.time.LocalDateTime;

public interface StatisticsService {
    ExhibitionCountDto getExhibitionCount(int interval, LocalDateTime now);

    UserCountDto getUserCount(int interval, LocalDateTime now);
}
