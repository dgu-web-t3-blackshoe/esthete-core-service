package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.SupportDto;
import com.blackshoe.esthetecoreservice.dto.UserDto;
import com.blackshoe.esthetecoreservice.entity.*;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.repository.SupportRepository;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
import com.blackshoe.esthetecoreservice.repository.ViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportServiceImpl implements SupportService {

    private final SupportRepository supportRepository;
    private final UserRepository userRepository;
    private final ViewRepository viewRepository;
    @Override
    @Transactional
    public SupportDto.CreateSupportResponse createSupport(UUID userId, SupportDto.CreateSupportRequest supportCreateSupportRequest) {

        final User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        final UUID photographerId = UUID.fromString(supportCreateSupportRequest.getPhotographerId());
        final User photographer = userRepository.findByUserId(photographerId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        final Support support = Support.builder()
                .photographer(photographer)
                .build();

        support.setUser(user);

        final Support savedSupport = supportRepository.save(support);

        final SupportDto.CreateSupportResponse supportCreateSupportResponse = SupportDto.CreateSupportResponse.builder()
                .supportId(savedSupport.getSupportId().toString())
                .createdAt(savedSupport.getCreatedAt().toString())
                .build();

        return supportCreateSupportResponse;
    }

    @Override
    @Transactional
    public SupportDto.DeleteSupportResponse deleteSupport(UUID userId, UUID photographerId) {

        final Support support = supportRepository.findByUserIdAndPhotographerId(userId, photographerId)
                .orElseThrow(() -> new UserException(UserErrorResult.SUPPORT_NOT_FOUND));

        support.unsetUser();

        supportRepository.delete(support);

        final SupportDto.DeleteSupportResponse supportDeleteSupportResponse = SupportDto.DeleteSupportResponse.builder()
                .supportId(support.getSupportId().toString())
                .deletedAt(LocalDateTime.now().toString())
                .build();

        return supportDeleteSupportResponse;
    }

    @Override
    public Page<UserDto.SearchResult> readSupportingPhotographers(UUID userId, String nickname, String sort, List<String> genres, int size, int page) {

        Page<User> photographers = null;

        Pageable pageable = PageRequest.of(page, size);

        if(sort == null) sort = "recent";

        if(sort.equals("recent")){
            if(genres == null)
                photographers = supportRepository.getPhotographersByRecentSupport(userId, pageable);
            else
                photographers = supportRepository.getPhotographersByRecentSupportAndGenres(userId, genres, pageable);
        }

        else if(sort.equals("popular")) {
            if(genres == null)
                photographers = supportRepository.getPhotographersBySupportCount(userId, pageable);
            else
                photographers = supportRepository.getPhotographersBySupportCountAndGenres(userId, genres, pageable);
        }

        else if(sort.equals("trending")){
            //최근 7일간 support를 많이 받은 순
            if(genres == null)
                photographers = supportRepository.getPhotographersBySupportCountInAWeek(userId, pageable);
            else
                photographers = supportRepository.getPhotographersBySupportCountInAWeekAndGenres(userId, genres, pageable);
        }

        Page<UserDto.SearchResult> photographersResponse = photographers.map(photographer -> new UserDto.SearchResult(photographer));

        return photographersResponse;

    }
}
