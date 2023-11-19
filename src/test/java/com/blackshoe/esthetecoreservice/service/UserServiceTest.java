package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.dto.UserDto;
import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.ProfileImgUrl;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.repository.ExhibitionRepository;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExhibitionRepository exhibitionRepository;

    @Mock
    private final User user = User.builder()
            .nickname("nickname")
            .biography("biography")
            .build();

    private final ProfileImgUrl profileImgUrl = ProfileImgUrl.builder()
            .cloudfrontUrl("cloudfrontUrl")
            .s3Url("s3Url")
            .build();

    private final UUID userId = UUID.randomUUID();

    @Mock
    private final Exhibition exhibition = Exhibition.builder()
            .title("test")
            .description("test")
            .thumbnail("thumbnail")
            .build();

    private final UUID exhibitionId = UUID.randomUUID();

    @Test
    public void readBasicInfo_returns_userGetBasicInfoResponse() {
        // given
        when(userRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(user));
        when(user.getUserId()).thenReturn(userId);
        when(user.getProfileImgUrl()).thenReturn(profileImgUrl);

        // when
        final UserDto.ReadBasicInfoResponse userReadBasicInfoResponse = userService.readBasicInfo(userId);

        // then
        verify(userRepository, times(1)).findByUserId(any(UUID.class));
        assertThat(userReadBasicInfoResponse).isNotNull();
        assertThat(userReadBasicInfoResponse.getUserId()).isEqualTo(userId.toString());
        assertThat(userReadBasicInfoResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(userReadBasicInfoResponse.getProfileImg()).isEqualTo(user.getProfileImgUrl().getCloudfrontUrl());
    }

    @Test
    public void readCurrentExhibitionOfUser_whenSuccess_returnsExhibitionUserCurrentResponse() {
        // given
        user.setProfileImgUrl(profileImgUrl);
        when(exhibitionRepository.findMostRecentExhibitionOfUser(any(UUID.class))).thenReturn(Optional.of(exhibition));
        when(exhibition.getExhibitionId()).thenReturn(exhibitionId);

        // when
        final ExhibitionDto.ReadCurrentOfUserResponse exhibitionReadCurrentOfUserResponse
                = userService.readCurrentExhibitionOfUser(userId);

        // then
        verify(exhibitionRepository).findMostRecentExhibitionOfUser(any(UUID.class));
        assertThat(exhibitionReadCurrentOfUserResponse.getExhibitionId()).isEqualTo(exhibitionId.toString());
        assertThat(exhibitionReadCurrentOfUserResponse.getTitle()).isEqualTo(exhibition.getTitle());
        assertThat(exhibitionReadCurrentOfUserResponse.getDescription()).isEqualTo(exhibition.getDescription());
        assertThat(exhibitionReadCurrentOfUserResponse.getThumbnail()).isEqualTo(exhibition.getThumbnail());
    }

    @Test
    public void readAllNicknameContaining_whenSuccess_returnsUserReadAllContainingNicknameResponse() {
        // given
        when(userRepository.findAllByNicknameContaining(any(String.class), any(Pageable.class))).thenReturn(Page.empty());

        // when
        final Page<UserDto.SearchResult> userReadAllNicknameContainingResponse
                = userService.readAllNicknameContaining("test", Pageable.unpaged());

        // then
        verify(userRepository).findAllByNicknameContaining(any(String.class), any(Pageable.class));
        assertThat(userReadAllNicknameContainingResponse).isNotNull();
    }

    @Test
    public void readAllGenresContaining_whenSuccess_returnsUserReadAllContainingNicknameResponse() {
        // given
        when(userRepository.findAllByGenresContaining(any(List.class), any(Pageable.class))).thenReturn(Page.empty());

        // when
        final Page<UserDto.SearchResult> userReadAllGenresContainingResponse
                = userService.readAllGenresContaining(List.of(), Pageable.unpaged());

        // then
        verify(userRepository).findAllByGenresContaining(any(List.class), any(Pageable.class));
        assertThat(userReadAllGenresContainingResponse).isNotNull();
    }

    @Test
    public void readAllNicknameAndGenreContaining_whenSuccess_returnsUserReadAllContainingNicknameResponse() {
        // given
        when(userRepository.findAllByNicknameAndGenresContaining(any(String.class), any(List.class), any(Pageable.class))).thenReturn(Page.empty());

        // when
        final Page<UserDto.SearchResult> userReadAllGenresContainingAndNicknameContainingResponse
                = userService.readAllNicknameAndGenreContaining("test", List.of(), Pageable.unpaged());

        // then
        verify(userRepository).findAllByNicknameAndGenresContaining(any(String.class), any(List.class), any(Pageable.class));
        assertThat(userReadAllGenresContainingAndNicknameContainingResponse).isNotNull();
    }
}
