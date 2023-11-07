package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.RoomDto;
import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.Room;
import com.blackshoe.esthetecoreservice.entity.RoomPhoto;
import com.blackshoe.esthetecoreservice.repository.ExhibitionRepository;
import com.blackshoe.esthetecoreservice.repository.PhotoRepository;
import com.blackshoe.esthetecoreservice.repository.RoomPhotoRepository;
import com.blackshoe.esthetecoreservice.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @InjectMocks
    private RoomServiceImpl roomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ExhibitionRepository exhibitionRepository;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private RoomPhotoRepository roomPhotoRepository;

    @Mock
    private final Room room = Room.builder()
            .title("test")
            .description("test")
            .thumbnail("thumbnail")
            .build();

    @Mock
    private final Exhibition exhibition = Exhibition.builder()
            .title("test")
            .description("test")
            .thumbnail("thumbnail")
            .build();

    private final UUID roomId = UUID.randomUUID();

    private final LocalDateTime createdAt = LocalDateTime.now();

    private final RoomDto.CreateRequest roomCreateRequest = RoomDto.CreateRequest.builder()
            .title("test")
            .description("test")
            .thumbnail("thumbnail")
            .photos(List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
            .build();

    @Test
    public void createRoom_whenSuccess_returnsRoomCreateResponse() {
        // given
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(exhibitionRepository.findByExhibitionId(any(UUID.class))).thenReturn(Optional.of(exhibition));
        when(photoRepository.findByPhotoId(any(UUID.class))).thenReturn(Optional.of(Photo.builder().build()));
        when(room.getRoomId()).thenReturn(roomId);
        when(room.getCreatedAt()).thenReturn(createdAt);

        // when
        final RoomDto.CreateResponse roomCreateResponse = roomService.createRoom(roomCreateRequest, UUID.randomUUID());

        // then
        verify(exhibitionRepository, times(1)).findByExhibitionId(any(UUID.class));
        verify(roomRepository, times(1)).save(any(Room.class));
        verify(photoRepository, times(roomCreateRequest.getPhotos().size())).findByPhotoId(any(UUID.class));
        verify(roomPhotoRepository, times(roomCreateRequest.getPhotos().size())).save(any(RoomPhoto.class));
        assertThat(roomCreateResponse.getRoomId()).isEqualTo(roomId.toString());
        assertThat(roomCreateResponse.getCreatedAt()).isEqualTo(createdAt.toString());
    }
}
