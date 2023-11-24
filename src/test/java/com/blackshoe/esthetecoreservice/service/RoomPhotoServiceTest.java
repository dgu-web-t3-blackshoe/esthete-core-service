package com.blackshoe.esthetecoreservice.service;


import com.blackshoe.esthetecoreservice.dto.RoomPhotoDto;
import com.blackshoe.esthetecoreservice.entity.Room;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.repository.RoomPhotoRepository;
import com.blackshoe.esthetecoreservice.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomPhotoServiceTest {

    @InjectMocks
    private RoomPhotoServiceImpl roomPhotoService;

    @Mock
    private RoomPhotoRepository roomPhotoRepository;

    @Spy
    private final Room room = Room.builder()
            .title("test")
            .description("test")
            .thumbnail("thumbnail")
            .build();

    @Test
    public void readExhibitionRoomPhotoList_whenSuccess_returnsRoomPhotoReadListResponse() {
        // given
        final RoomPhotoDto roomPhotoDto = RoomPhotoDto.builder()
                .roomId(UUID.randomUUID().toString())
                .photoId(UUID.randomUUID().toString())
                .title("test")
                .photo("photo")
                .userId(UUID.randomUUID().toString())
                .nickname("nickname")
                .build();

        final List<RoomPhotoDto> roomPhotos = List.of(roomPhotoDto);

        when(roomPhotoRepository.findAllByRoomId(any(UUID.class))).thenReturn(roomPhotos);

        // when
        final RoomPhotoDto.ReadListResponse roomPhotoReadListResponse = roomPhotoService.readRoomPhotoList(UUID.randomUUID());

        // then
        verify(roomPhotoRepository, times(1)).findAllByRoomId(any(UUID.class));
        assertThat(roomPhotoReadListResponse.getRoomPhotos().get(0).getRoomId()).isEqualTo(roomPhotoDto.getRoomId());
        assertThat(roomPhotoReadListResponse.getRoomPhotos().get(0).getPhotoId()).isEqualTo(roomPhotoDto.getPhotoId());
        assertThat(roomPhotoReadListResponse.getRoomPhotos().get(0).getTitle()).isEqualTo(roomPhotoDto.getTitle());
        assertThat(roomPhotoReadListResponse.getRoomPhotos().get(0).getPhoto()).isEqualTo(roomPhotoDto.getPhoto());
        assertThat(roomPhotoReadListResponse.getRoomPhotos().get(0).getUserId()).isEqualTo(roomPhotoDto.getUserId());
        assertThat(roomPhotoReadListResponse.getRoomPhotos().get(0).getNickname()).isEqualTo(roomPhotoDto.getNickname());
    }

    @Test
    public void readExhibitionRoomPhotoList_whenEmptyRoom_throwsExhibitionException() {
        // given
        final List<RoomPhotoDto> roomPhotos = List.of();

        // when
        when(roomPhotoRepository.findAllByRoomId(any(UUID.class))).thenReturn(roomPhotos);

        // then
        assertThrows(ExhibitionException.class, () -> roomPhotoService.readRoomPhotoList(UUID.randomUUID()));
    }
}
