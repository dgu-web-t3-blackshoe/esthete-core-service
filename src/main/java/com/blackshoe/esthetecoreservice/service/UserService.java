package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto.ReadBasicInfoResponse readBasicInfo(UUID userId);

    ExhibitionDto.ReadCurrentOfUserResponse readCurrentExhibitionOfUser(UUID userId);

    Page<UserDto.SearchResult> readAllNicknameContaining(String nickname, Pageable pageable);

    Page<UserDto.SearchResult> readAllGenresContaining(List<UUID> genres, Pageable pageable);

    Page<UserDto.SearchResult> readAllNicknameAndGenreContaining(String nickname, List<UUID> genres, Pageable pageable);
}
