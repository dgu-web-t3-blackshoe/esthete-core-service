package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.GuestBookDto;
import com.blackshoe.esthetecoreservice.entity.GuestBook;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.repository.GuestBookRepository;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestBookServiceImpl implements GuestBookService {

    private final UserRepository userRepository;

    private final GuestBookRepository guestBookRepository;

    @Override
    @Transactional
    public GuestBookDto.CreateGuestBookResponse createGuestBook(UUID photographerId, GuestBookDto.CreateGuestBookRequest guestBookCreateGuestBookRequest) {
        final User photographer = userRepository.findByUserId(photographerId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        final UUID userId = UUID.fromString(guestBookCreateGuestBookRequest.getUserId());
        final User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        final GuestBook guestBook = GuestBook.builder()
                .user(user)
                .content(guestBookCreateGuestBookRequest.getContent())
                .build();

        guestBook.setPhotographer(photographer);

        final GuestBook savedGuestBook = guestBookRepository.save(guestBook);

        final GuestBookDto.CreateGuestBookResponse guestBookCreateGuestBookResponse = GuestBookDto.CreateGuestBookResponse.builder()
                .guestBookId(savedGuestBook.getGuestBookId().toString())
                .createdAt(savedGuestBook.getCreatedAt().toString())
                .build();

        return guestBookCreateGuestBookResponse;
    }
}
