package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.*;
import com.blackshoe.esthetecoreservice.service.UserService;
import com.blackshoe.esthetecoreservice.vo.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.blackshoe.esthetecoreservice.dto.UserDto;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.service.GuestBookService;
import com.blackshoe.esthetecoreservice.service.SupportService;
import com.blackshoe.esthetecoreservice.service.UserService;
import com.blackshoe.esthetecoreservice.vo.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
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
    }

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
    public ResponseEntity<Page<PhotoDto.ReadResponse>> getUserPhotos(@PathVariable UUID userId, @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(required = false, defaultValue = "recent") String sort) {
        final Sort sortBy = SortType.convertParamToColumn(sort);

        Page<PhotoDto.ReadResponse> readUserPhotos = userService.readUserPhotos(userId, sortBy, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(readUserPhotos);
    }

    @GetMapping("/{userId}/exhibitions")
    public ResponseEntity<Page<ExhibitionDto.ReadResponse>> getUserExhibitions(@PathVariable UUID userId,
                                                                               @RequestParam(defaultValue = "10") int size,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(required = false, defaultValue = "recent") String sort) {
        final Sort sortBy = SortType.convertParamToColumn(sort);

        Page<ExhibitionDto.ReadResponse> readUserExhibitions = userService.readUserExhibitions(userId, sortBy, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(readUserExhibitions);
    }

    @GetMapping("/{userId}/guest-books")
    public ResponseEntity<Page<GuestBookDto.ReadResponse>> getUserGuestBooks(@PathVariable UUID userId,
                                                                             @RequestParam(defaultValue = "10") int size,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(required = false, defaultValue = "recent") String sort) {

        final Sort sortBy = SortType.convertParamToColumn(sort);

        Page<GuestBookDto.ReadResponse> readUserGuestBooksPage = userService.readUserGuestbooks(userId, sortBy, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(readUserGuestBooksPage);
    }
    @GetMapping("/search")
    public ResponseEntity<Page<UserDto.SearchResult>> searchByNicknameOrGenre(@RequestParam(required = false) Optional<String> nickname,
                                                                 @RequestParam(required = false) Optional<List<UUID>> genres,
                                                                 @RequestParam(required = false, defaultValue = "recent") String sort,
                                                                 @RequestParam(required = false, defaultValue = "0") Integer page,
                                                                 @RequestParam(required = false, defaultValue = "10") Integer size) {

        final Sort sortBy = SortType.convertParamToColumn(sort);
        final Pageable pageable = PageRequest.of(page, size, sortBy);

        Page<UserDto.SearchResult> readAllNicknameContainingPage = Page.empty();

        if (nickname.isPresent() && genres.isEmpty()) {
            readAllNicknameContainingPage = userService.readAllNicknameContaining(nickname.get(), pageable);
        }
        if (nickname.isEmpty() && genres.isPresent()) {
            readAllNicknameContainingPage = userService.readAllGenresContaining(genres.get(), pageable);
        }
        if (nickname.isPresent() && genres.isPresent()) {
            readAllNicknameContainingPage = userService.readAllNicknameAndGenreContaining(nickname.get(), genres.get(), pageable);
        }

        return ResponseEntity.status(HttpStatus.OK).body(readAllNicknameContainingPage);
    }
}
