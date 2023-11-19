package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.SupportDto;

import java.util.List;
import java.util.UUID;

public interface SupportService {

    SupportDto.CreateResponse createSupport(UUID userId, SupportDto.CreateRequest supportCreateRequest);

    SupportDto.DeleteResponse deleteSupport(UUID userId, UUID photographerId);

    SupportDto.ReadSupportingPhotographersResponse readSupportingPhotographers(UUID userId, String nickname, String sort, List<String> genres, int size, int page);
}
