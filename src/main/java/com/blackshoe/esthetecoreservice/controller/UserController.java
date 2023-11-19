package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.dto.GuestBookDto;
import com.blackshoe.esthetecoreservice.dto.SupportDto;
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
