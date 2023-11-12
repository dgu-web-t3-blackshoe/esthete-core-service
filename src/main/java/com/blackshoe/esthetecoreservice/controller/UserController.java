package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.dto.GuestBookDto;
import com.blackshoe.esthetecoreservice.dto.SupportDto;
import com.blackshoe.esthetecoreservice.dto.UserDto;
import com.blackshoe.esthetecoreservice.service.GuestBookService;
import com.blackshoe.esthetecoreservice.service.SupportService;
import com.blackshoe.esthetecoreservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final GuestBookService guestBookService;

    private final SupportService supportService;

    @GetMapping("/{userId}/basic-info")
    public ResponseEntity<UserDto.ReadBasicInfoResponse> getBasicInfo(@PathVariable UUID userId) {

        UserDto.ReadBasicInfoResponse userGetBasicInfoResponse = userService.readBasicInfo(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userGetBasicInfoResponse);
    }

    @GetMapping("/{userId}/exhibitions/current")
    public ResponseEntity<ExhibitionDto.ReadCurrentOfUserResponse> getCurrentExhibitionOfUser(@PathVariable UUID userId) {

        ExhibitionDto.ReadCurrentOfUserResponse userReadCurrentExhibitionOfUserResponse = userService.readCurrentExhibitionOfUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userReadCurrentExhibitionOfUserResponse);
    }

    @PostMapping("/{photographerId}/guest-books")
    public ResponseEntity<GuestBookDto.CreateResponse> createGuestBook(@PathVariable UUID photographerId, @Valid @RequestBody GuestBookDto.CreateRequest guestBookCreateRequest) {

        GuestBookDto.CreateResponse guestBookCreateResponse = guestBookService.createGuestBook(photographerId, guestBookCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(guestBookCreateResponse);
    }

    @PostMapping("/{userId}/supports")
    public ResponseEntity<SupportDto.CreateResponse> createSupport(@PathVariable UUID userId, @Valid @RequestBody SupportDto.CreateRequest supportCreateRequest) {

        SupportDto.CreateResponse supportCreateResponse = supportService.createSupport(userId, supportCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(supportCreateResponse);
    }

    @DeleteMapping("/{userId}/supports/{photographerId}")
    public ResponseEntity<SupportDto.DeleteResponse> deleteSupport(@PathVariable UUID userId, @PathVariable UUID photographerId) {

        SupportDto.DeleteResponse supportDeleteResponse = supportService.deleteSupport(userId, photographerId);

        return ResponseEntity.status(HttpStatus.OK).body(supportDeleteResponse);
    }
}
