package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.SupportDto;
import com.blackshoe.esthetecoreservice.dto.UserDto;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

public interface SupportService {

    SupportDto.CreateResponse createSupport(UUID userId, SupportDto.CreateRequest supportCreateRequest);

    SupportDto.DeleteResponse deleteSupport(UUID userId, UUID photographerId);

    Page<UserDto.SearchResult> readSupportingPhotographers(UUID userId, String nickname, String sort, List<String> genres, int size, int page);
}
