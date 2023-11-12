package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.RoomDto;
import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.Room;
import com.blackshoe.esthetecoreservice.entity.RoomPhoto;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.exception.PhotoErrorResult;
import com.blackshoe.esthetecoreservice.exception.PhotoException;
import com.blackshoe.esthetecoreservice.repository.ExhibitionRepository;
import com.blackshoe.esthetecoreservice.repository.PhotoRepository;
import com.blackshoe.esthetecoreservice.repository.RoomPhotoRepository;
import com.blackshoe.esthetecoreservice.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final ExhibitionRepository exhibitionRepository;

    private final RoomRepository roomRepository;

    private final PhotoRepository photoRepository;

    private final RoomPhotoRepository roomPhotoRepository;

    @Override
    @Transactional
    public RoomDto.CreateResponse createRoom(RoomDto.CreateRequest roomCreateRequest, UUID exhibitionId) {

        final Exhibition exhibition = exhibitionRepository.findByExhibitionId(exhibitionId)
                .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.EXHIBITION_NOT_FOUND));

        final Room room = Room.builder()
                .title(roomCreateRequest.getTitle())
                .description(roomCreateRequest.getDescription())
                .thumbnail(roomCreateRequest.getThumbnail())
                .build();

        room.setExhibition(exhibition);

        final Room savedRoom = roomRepository.save(room);

        final List<String> roomPhotoIds = roomCreateRequest.getPhotos();

        roomPhotoIds.stream().forEach(roomPhotoId -> {

            final Photo photo = photoRepository.findByPhotoId(UUID.fromString(roomPhotoId))
                    .orElseThrow(() -> new PhotoException(PhotoErrorResult.PHOTO_NOT_FOUND));

            final RoomPhoto roomPhoto = RoomPhoto.builder()
                    .photo(photo)
                    .build();

            roomPhoto.setRoom(savedRoom);

            roomPhotoRepository.save(roomPhoto);

        });

        final RoomDto.CreateResponse roomCreateResponseDto = RoomDto.CreateResponse.builder()
                .roomId(savedRoom.getRoomId().toString())
                .createdAt(savedRoom.getCreatedAt().toString())
                .build();

        return roomCreateResponseDto;
    }

    @Override
    public RoomDto.DeleteResponse deleteRoom(UUID roomId) {

        final Room room = roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.ROOM_NOT_FOUND));

        roomRepository.delete(room);

        final RoomDto.DeleteResponse roomDeleteResponseDto = RoomDto.DeleteResponse.builder()
                .roomId(room.getRoomId().toString())
                .deletedAt(LocalDateTime.now().toString())
                .build();

        return roomDeleteResponseDto;
    }

    @Override
    public RoomDto.ReadListResponse readExhibitionRoomList(UUID exhibitionId) {

        final List<RoomDto> roomDtoList = roomRepository.findAllByExhibitionId(exhibitionId);

        final RoomDto.ReadListResponse roomReadListResponseDto = RoomDto.ReadListResponse.builder()
                .rooms(roomDtoList)
                .build();

        return roomReadListResponseDto;
    }
}
