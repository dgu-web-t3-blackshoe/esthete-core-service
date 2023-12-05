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
import org.springframework.data.domain.Sort;
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
    public Page<UserDto.SearchResult> readAllByNicknameContaining(UUID supporterId, String nickname, Pageable pageable) {

        final Page<UserDto.SearchResult> searchResultPage = supportRepository.findAllByNicknameContaining(supporterId, nickname, pageable);

        return searchResultPage;
    }

    @Override
    public Page<UserDto.SearchResult> readAllByGenresContaining(UUID supporterId, List<UUID> searchGenreIds, Pageable pageable) {

        final Page<UserDto.SearchResult> searchResultPage = supportRepository.findAllByGenreContaining(supporterId, searchGenreIds, pageable);

        return searchResultPage;
    }

    @Override
    public Page<UserDto.SearchResult> readAllByNicknameAndGenresContaining(UUID supporterId, String nickname, List<UUID> searchGenreIds, Pageable pageable) {

        final Page<UserDto.SearchResult> searchResultPage = supportRepository.findAllByNicknameAndGenresContaining(supporterId, nickname, searchGenreIds, pageable);

        return searchResultPage;
    }
    @Override
    public SupportDto.IsSupported getIsSupported(UUID userId, UUID photographerId) {

        final User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        final User photographer = userRepository.findByUserId(photographerId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        final Boolean isSupported = supportRepository.existsByUserAndPhotographer(user, photographer);

        final SupportDto.IsSupported isSupportedResponse = SupportDto.IsSupported.builder()
                .isSupported(isSupported)
                .build();

        return isSupportedResponse;
    }
}
