package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.SupportDto;
import com.blackshoe.esthetecoreservice.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface SupportService {

    SupportDto.CreateSupportResponse createSupport(UUID userId, SupportDto.CreateSupportRequest supportCreateSupportRequest);

    SupportDto.DeleteSupportResponse deleteSupport(UUID userId, UUID photographerId);

    Page<UserDto.SearchResult> readAllByNicknameContaining(UUID supporterId, String nickname, Pageable pageable);
    Page<UserDto.SearchResult> readAllByGenresContaining(UUID supporterId, List<UUID> searchGenreIds, Pageable pageable);
    Page<UserDto.SearchResult>  readAllByNicknameAndGenresContaining(UUID supporterId, String nickname, List<UUID> searchGenreIds, Pageable pageable);


    SupportDto.IsSupported getIsSupported(UUID userId, UUID photographerId);
}
