package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.SupportDto;
import com.blackshoe.esthetecoreservice.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface SupportService {

    SupportDto.CreateSupportResponse createSupport(UUID userId, SupportDto.CreateSupportRequest supportCreateSupportRequest);

    SupportDto.DeleteSupportResponse deleteSupport(UUID userId, UUID photographerId);

    Page<UserDto.SearchResult> readSupportingPhotographers(UUID userId, String nickname, String sort, List<String> genres, int size, int page);

    SupportDto.IsSupported getIsSupported(UUID userId, UUID photographerId);
}
