package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.SupportDto;
import com.blackshoe.esthetecoreservice.entity.Support;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.repository.SupportRepository;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
}
