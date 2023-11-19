package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.SupportDto;
import com.blackshoe.esthetecoreservice.entity.*;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.repository.SupportRepository;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportServiceImpl implements SupportService {

    private final SupportRepository supportRepository;
    private final UserRepository userRepository;

    @Override
    public SupportDto.CreateResponse createSupport(UUID userId, SupportDto.CreateRequest supportCreateRequest) {

        final User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        final UUID photographerId = UUID.fromString(supportCreateRequest.getPhotographerId());
        final User photographer = userRepository.findByUserId(photographerId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        final Support support = Support.builder()
                .photographer(photographer)
                .build();

        support.setUser(user);

        final Support savedSupport = supportRepository.save(support);

        final SupportDto.CreateResponse supportCreateResponse = SupportDto.CreateResponse.builder()
                .supportId(savedSupport.getSupportId().toString())
                .createdAt(savedSupport.getCreatedAt().toString())
                .build();

        return supportCreateResponse;
    }

    @Override
    public SupportDto.DeleteResponse deleteSupport(UUID userId, UUID photographerId) {

        final Support support = supportRepository.findByUserIdAndPhotographerId(userId, photographerId)
                .orElseThrow(() -> new UserException(UserErrorResult.SUPPORT_NOT_FOUND));

        supportRepository.delete(support);

        final SupportDto.DeleteResponse supportDeleteResponse = SupportDto.DeleteResponse.builder()
                .supportId(support.getSupportId().toString())
                .deletedAt(LocalDateTime.now().toString())
                .build();

        return supportDeleteResponse;
    }

    @Override
    public SupportDto.ReadSupportingPhotographersResponse readSupportingPhotographers(UUID userId, String nickname, String sort, List<String> genres, int size, int page) {

        Page<User> photographers = null;

        Pageable pageable = PageRequest.of(page, size);

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

        SupportDto.ReadSupportingPhotographersResponse supportingPhotographersResponse = SupportDto.ReadSupportingPhotographersResponse.builder()
                .content(new ArrayList<>())
                .build();

        //genres 내 genreName과 일치하는 photographer의 genreName을 가져온다.
        //photographer의 highlightId를 가져온다.

        for(User photographer : photographers){
            SupportDto.ReadSupportingPhotographer readSupportingPhotographer = SupportDto.ReadSupportingPhotographer.builder()
                    .photographerId(photographer.getUserId().toString())
                    .profileImg(photographer.getProfileImgUrl().getCloudfrontUrl())
                    .nickname(photographer.getNickname())
                    .biography(photographer.getBiography())
                    .genres(new ArrayList<>())
                    .highlights(new ArrayList<>())
                    .build();

            for (UserGenre userGenre : photographer.getUserGenres()) {
                String genreName = userGenre.getGenre().getGenreName();
                readSupportingPhotographer.getGenres().add(genreName);
            }


            for (Highlight highlight : photographer.getHighlights()) {
                String highlightId = highlight.getHighlightId().toString();
                readSupportingPhotographer.getHighlights().add(highlightId);
            }

            supportingPhotographersResponse.addReadSupportingPhotographer(readSupportingPhotographer);
        }

        return supportingPhotographersResponse;
    }
}
