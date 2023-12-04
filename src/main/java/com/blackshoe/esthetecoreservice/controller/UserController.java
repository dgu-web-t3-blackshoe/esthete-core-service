package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.*;
import com.blackshoe.esthetecoreservice.service.ProfileImgService;
import com.blackshoe.esthetecoreservice.service.UserService;
import com.blackshoe.esthetecoreservice.vo.PhotoSortType;
import com.blackshoe.esthetecoreservice.vo.UserSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.blackshoe.esthetecoreservice.dto.UserDto;
import com.blackshoe.esthetecoreservice.service.GuestBookService;
import com.blackshoe.esthetecoreservice.service.SupportService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;
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

    private final ProfileImgService profileImgService;

    @GetMapping("/{user_id}/equipments")
    ResponseEntity<UserDto.ReadEquipmentsResponse> getEquipments(@PathVariable(name = "user_id") UUID userId) {
        //getEquipmentsForUser
        UserDto.ReadEquipmentsResponse response = userService.getEquipmentsForUser(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{user_id}/basic-info")
    public ResponseEntity<UserDto.ReadBasicInfoResponse> getBasicInfo(@PathVariable(name = "user_id") UUID userId) {

        UserDto.ReadBasicInfoResponse userGetBasicInfoResponse = userService.readBasicInfo(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userGetBasicInfoResponse);
    }

    @GetMapping("/{user_id}/exhibitions/current")
    public ResponseEntity<ExhibitionDto.ReadCurrentOfUserExhibitionResponse> getCurrentExhibitionOfUser(@PathVariable(name = "user_id") UUID userId) {

        ExhibitionDto.ReadCurrentOfUserExhibitionResponse userReadCurrentExhibitionOfUserResponse = userService.readCurrentExhibitionOfUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userReadCurrentExhibitionOfUserResponse);
    }

    @PostMapping("/{photographer_id}/guest-books")
    public ResponseEntity<GuestBookDto.CreateGuestBookResponse> createGuestBook(@PathVariable(name = "photographer_id") UUID photographerId, @Valid @RequestBody GuestBookDto.CreateGuestBookRequest guestBookCreateGuestBookRequest) {

        GuestBookDto.CreateGuestBookResponse guestBookCreateGuestBookResponse = guestBookService.createGuestBook(photographerId, guestBookCreateGuestBookRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(guestBookCreateGuestBookResponse);
    }

    @PostMapping("/{user_id}/supports")
    public ResponseEntity<SupportDto.CreateSupportResponse> createSupport(@PathVariable(name = "user_id") UUID userId, @Valid @RequestBody SupportDto.CreateSupportRequest supportCreateSupportRequest) {

        SupportDto.CreateSupportResponse supportCreateSupportResponse = supportService.createSupport(userId, supportCreateSupportRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(supportCreateSupportResponse);
    }

    @DeleteMapping("/{user_id}/supports/{photographerId}")
    public ResponseEntity<SupportDto.DeleteSupportResponse> deleteSupport(@PathVariable(name = "user_id") UUID userId, @PathVariable UUID photographerId) {

        SupportDto.DeleteSupportResponse supportDeleteSupportResponse = supportService.deleteSupport(userId, photographerId);

        return ResponseEntity.status(HttpStatus.OK).body(supportDeleteSupportResponse);
    }

    @GetMapping("/{user_id}/supports/all")
    public ResponseEntity<Page<UserDto.SearchResult>> getUserSupports(
            @PathVariable(name = "user_id") UUID userId,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) List<String> genres,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page) {


        Page<UserDto.SearchResult> readSupportingPhotographersPage = supportService.readSupportingPhotographers(userId, nickname, sort, genres, size, page);
        return ResponseEntity.status(HttpStatus.OK).body(readSupportingPhotographersPage);
    }

    @GetMapping("/{user_id}/photos")
    public ResponseEntity<Page<PhotoDto.ReadPhotoResponse>> getUserPhotos(@PathVariable(name = "user_id") UUID userId, @RequestParam(defaultValue = "10") int size,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(required = false, defaultValue = "recent") String sort) {

        final Sort sortBy = PhotoSortType.convertParamToColumn(sort);

        Page<PhotoDto.ReadPhotoResponse> readUserPhotos = userService.readUserPhotos(userId, sortBy, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(readUserPhotos);
    }

    @GetMapping("/{user_id}/exhibitions")
    public ResponseEntity<Page<ExhibitionDto.ReadExhibitionResponse>> getUserExhibitions(@PathVariable(name = "user_id") UUID userId,
                                                                                         @RequestParam(defaultValue = "10") int size,
                                                                                         @RequestParam(defaultValue = "0") int page,
                                                                                         @RequestParam(required = false, defaultValue = "recent") String sort) {
        final Sort sortBy = PhotoSortType.convertParamToColumn(sort);

        Page<ExhibitionDto.ReadExhibitionResponse> readUserExhibitions = userService.readUserExhibitions(userId, sortBy, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(readUserExhibitions);
    }

    @GetMapping("/{user_id}/guest-books")
    public ResponseEntity<Page<GuestBookDto.ReadGuestBookResponse>> getUserGuestBooks(@PathVariable(name = "user_id") UUID userId,
                                                                                      @RequestParam(defaultValue = "10") int size,
                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(required = false, defaultValue = "recent") String sort) {
        final Sort sortBy = UserSortType.convertParamToColumn(sort);

        Page<GuestBookDto.ReadGuestBookResponse> readUserGuestBooksPage = userService.readUserGuestbooks(userId, sortBy, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(readUserGuestBooksPage);
    }
    @GetMapping("/search")
    public ResponseEntity<Page<UserDto.SearchResult>> searchByNicknameOrGenre(@RequestParam(required = false) Optional<String> nickname,
                                                                 @RequestParam(required = false) Optional<List<UUID>> genres,
                                                                 @RequestParam(required = false, defaultValue = "recent") String sort,
                                                                 @RequestParam(required = false, defaultValue = "0") Integer page,
                                                                 @RequestParam(required = false, defaultValue = "10") Integer size) {

        final Sort sortBy = UserSortType.convertParamToColumn(sort);
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

    @DeleteMapping("/{user_id}")
    public ResponseEntity<UserDto.DeleteResponse> deleteUser(@PathVariable(name = "user_id") UUID userId) {

        UserDto.DeleteResponse userDeleteResponse = userService.deleteUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userDeleteResponse);
    }

    @PostMapping(value = "/{user_id}/sign-up") // SignUpInfoRequest -> SignUpRequest
    public ResponseEntity<UserDto.SignUpResponse> signUp(@PathVariable(name = "user_id") UUID userId, @Valid @RequestBody UserDto.SignUpRequest userSignUpRequest) {

        UserDto.SignUpResponse userSignUpResponse = userService.signUp(userId, userSignUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(userSignUpResponse);
    }

    @GetMapping("/{user_id}/profile") //API - 100
    public ResponseEntity<UserDto.MyProfileInfoResponse> getUserMyProfile(@PathVariable(name = "user_id") UUID userId) throws Exception{
        UserDto.MyProfileInfoResponse myProfileInfoResponse = userService.getMyProfileInfo(userId);

        return ResponseEntity.status(HttpStatus.OK).body(myProfileInfoResponse); //200
    }

    @PutMapping(value = "/{user_id}/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserDto.UpdateProfileResponse> updateProfileImage(@PathVariable(name = "user_id") UUID userId,
                                                                            @RequestPart UserDto.UpdateProfileRequest userUpdateProfileRequest,
                                                                            @RequestPart(name = "profile_img", required = false) MultipartFile profileImg) throws Exception {

        profileImgService.deleteProfileImg(userId);
        ProfileImgUrlDto profileImgUrlDto;
        if(profileImg == null){
            profileImgUrlDto = profileImgService.getUserPresentProfileImgUrlDto(userId);
        }else{
            profileImgService.deleteProfileImg(userId);
            profileImgUrlDto = profileImgService.uploadProfileImg(userId, profileImg);
        }

        UserDto.UpdateProfileDto updateProfileDto = UserDto.UpdateProfileDto.builder()
                .profileImgUrlDto(profileImgUrlDto)
                .nickname(userUpdateProfileRequest.getNickname())
                .biography(userUpdateProfileRequest.getBiography())
                .genres(userUpdateProfileRequest.getGenres())
                .equipments(userUpdateProfileRequest.getEquipments())
                .build();

        UserDto.UpdateProfileResponse userUpdateProfileResponse = userService.updateMyProfile(userId, updateProfileDto);

        return ResponseEntity.status(HttpStatus.OK).body(userUpdateProfileResponse); //200
    }

    @PostMapping(value = "/{user_id}/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}) // API - 142
    public ResponseEntity<UserDto.UpdateProfileImgResponse> uploadProfileImage(@RequestPart(name = "profile_img", required = false) MultipartFile profileImg,
                                                     @PathVariable(name = "user_id") UUID userId) throws Exception {

        ProfileImgUrlDto profileImgUrlDto = profileImgService.uploadProfileImg(userId, profileImg);

        UserDto.UpdateProfileImgResponse updateProfileImgResponse = UserDto.UpdateProfileImgResponse.builder()
                .userId(userId.toString())
                .profileImg(profileImgUrlDto.getCloudfrontUrl())
                .updatedAt(String.valueOf(LocalDateTime.now()))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(updateProfileImgResponse); //200
    }


}
