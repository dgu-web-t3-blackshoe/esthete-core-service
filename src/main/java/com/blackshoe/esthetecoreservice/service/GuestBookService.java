package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.GuestBookDto;

import java.util.UUID;

public interface GuestBookService {

    GuestBookDto.CreateGuestBookResponse createGuestBook(UUID photographerId, GuestBookDto.CreateGuestBookRequest guestBookCreateGuestBookRequest);
}
