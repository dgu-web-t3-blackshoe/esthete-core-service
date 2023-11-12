package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.GuestBookDto;

import java.util.UUID;

public interface GuestBookService {

    GuestBookDto.CreateResponse createGuestBook(UUID photographerId, GuestBookDto.CreateRequest guestBookCreateRequest);
}
