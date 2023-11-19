package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.UserDto;
import com.blackshoe.esthetecoreservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/core/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final GuestBookService guestBookService;

    private final SupportService supportService;

    @GetMapping("/{user_id}/equipments")
    ResponseEntity<UserDto.ReadEquipmentsResponse> getEquipments(@PathVariable(name = "user_id") UUID userId) {
        //getEquipmentsForUser
        UserDto.ReadEquipmentsResponse response = userService.getEquipmentsForUser(userId);

        return ResponseEntity.ok(response);

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

    @GetMapping("/{userId}/supports/all")
    public ResponseEntity<SupportDto.ReadSupportingPhotographersResponse> getUserSupports(
            @PathVariable("userId") UUID userId,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) List<String> genres,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page) {

        //sort = recent or popular or trending
        SupportDto.ReadSupportingPhotographersResponse readSupportingPhotographersResponse = supportService.readSupportingPhotographers(userId, nickname, sort, genres, size, page);

        return ResponseEntity.ok().body(readSupportingPhotographersResponse);
    }

    @GetMapping("/{userId}/photos")
    public ResponseEntity<List<UserDto.ReadUserPhotosResponse>> getUserPhotos(@PathVariable UUID userId) {

        List<UserDto.ReadUserPhotosResponse> content = userService.readUserPhotos(userId);

        return ResponseEntity.status(HttpStatus.OK).body(content);
    }

    @GetMapping("/{userId}/exhibitions")
    public ResponseEntity<List<UserDto.ReadUserExhibitionResponse>> getUserExhibitions(@PathVariable UUID userId) {

        List<UserDto.ReadUserExhibitionResponse> content = userService.readUserExhibitions(userId);

        return ResponseEntity.status(HttpStatus.OK).body(content);
    }

    @GetMapping("/{userId}/guest-books")
    public ResponseEntity<List<UserDto.ReadUserGuestbookResponse>> getUserGuestBooks(@PathVariable UUID userId) {

        List<UserDto.ReadUserGuestbookResponse> content = userService.readUserGuestbooks(userId);

        return ResponseEntity.status(HttpStatus.OK).body(content);
    }
}
