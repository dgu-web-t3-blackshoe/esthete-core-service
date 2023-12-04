package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.GuestBookDto;
import com.blackshoe.esthetecoreservice.service.GuestBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/core/guest-books")
@RequiredArgsConstructor
public class GuestBookController {

    private final GuestBookService guestBookService;

    @DeleteMapping("/{guestBookId}")
    public ResponseEntity<GuestBookDto.DeleteGuestBookResponse> deleteGuestBook(@PathVariable("guestBookId") UUID guestBookId) {

        GuestBookDto.DeleteGuestBookResponse guestBookDeleteResponse = guestBookService.deleteGuestBook(guestBookId);

        return ResponseEntity.status(HttpStatus.OK).body(guestBookDeleteResponse);
    }
}
