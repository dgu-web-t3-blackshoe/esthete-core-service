package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionCountDto;
import com.blackshoe.esthetecoreservice.dto.UserCountDto;
import com.blackshoe.esthetecoreservice.repository.ExhibitionRepository;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final ExhibitionRepository exhibitionRepository;

    private final UserRepository userRepository;

    @Override
    public ExhibitionCountDto getExhibitionCount(int interval, LocalDateTime now) {

        ExhibitionCountDto exhibitionCountDto = exhibitionRepository.getExhibitionCount(now, interval);

        return exhibitionCountDto;
    }

    @Override
    public UserCountDto getUserCount(int interval, LocalDateTime now) {

        UserCountDto userCountDto = userRepository.getUserCount(now, interval);

        return userCountDto;
    }
}
