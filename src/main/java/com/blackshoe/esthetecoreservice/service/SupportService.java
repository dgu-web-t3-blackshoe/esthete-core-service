package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.SupportDto;

import java.util.UUID;

public interface SupportService {

    SupportDto.CreateResponse createSupport(UUID userId, SupportDto.CreateRequest supportCreateRequest);
}
