package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.GuestBookDto;
import com.blackshoe.esthetecoreservice.entity.GuestBook;
import com.blackshoe.esthetecoreservice.entity.ProfileImgUrl;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.repository.GuestBookRepository;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class GuestBookServiceTest {

    @InjectMocks
    private GuestBookServiceImpl guestBookService;

    @Mock
    private GuestBookRepository guestBookRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private final User photographer = User.builder()
            .nickname("photographer")
            .biography("biography")
            .build();

    private final UUID photographerId = UUID.randomUUID();

    private final ProfileImgUrl photographerProfileImgUrl = ProfileImgUrl.builder()
            .cloudfrontUrl("cloudfrontUrl")
            .s3Url("s3Url")
            .build();

    @Spy
    private final User user = User.builder()
            .nickname("user")
            .biography("biography")
            .build();

    private final ProfileImgUrl userProfileImgUrl = ProfileImgUrl.builder()
            .cloudfrontUrl("cloudfrontUrl")
            .s3Url("s3Url")
            .build();

    private final UUID userId = UUID.randomUUID();

    @Spy
    private final GuestBook guestbook = GuestBook.builder()
            .user(user)
            .content("content")
            .build();

    private final UUID guestbookId = UUID.randomUUID();

    private final LocalDateTime createdAt = LocalDateTime.now();

    @Test
    public void createGuestBook_whenSuccess_returnsCreateResponse() {
        // given
        when(userRepository.findByUserId(photographerId)).thenReturn(Optional.of(photographer));
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(guestBookRepository.save(any(GuestBook.class))).thenReturn(guestbook);
        when(guestbook.getGuestBookId()).thenReturn(guestbookId);
        when(guestbook.getCreatedAt()).thenReturn(createdAt);
        GuestBookDto.CreateRequest guestBookCreateRequest = GuestBookDto.CreateRequest.builder()
                .userId(userId.toString())
                .content("content")
                .build();

        // when
        final GuestBookDto.CreateResponse guestBookCreateResponse = guestBookService.createGuestBook(photographerId, guestBookCreateRequest);

        // then
        verify(userRepository, times(1)).findByUserId(photographerId);
        verify(userRepository, times(1)).findByUserId(userId);
        verify(guestBookRepository, times(1)).save(any(GuestBook.class));
        assertThat(guestBookCreateResponse).isNotNull();
        assertThat(guestBookCreateResponse.getGuestBookId()).isNotNull();
        assertThat(guestBookCreateResponse.getCreatedAt()).isNotNull();
    }
}
