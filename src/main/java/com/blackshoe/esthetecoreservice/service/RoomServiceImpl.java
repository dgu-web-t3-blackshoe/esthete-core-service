package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.RoomDto;
import com.blackshoe.esthetecoreservice.entity.*;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.exception.PhotoErrorResult;
import com.blackshoe.esthetecoreservice.exception.PhotoException;
import com.blackshoe.esthetecoreservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final ExhibitionRepository exhibitionRepository;

    private final RoomRepository roomRepository;

    private final PhotoRepository photoRepository;

    private final RoomPhotoRepository roomPhotoRepository;

    private final GenreRepository genreRepository;

    @Override
    @Transactional
    public RoomDto.CreateRoomResponse createRoom(RoomDto.CreateRoomRequest roomCreateRoomRequest, UUID exhibitionId) {

        final Exhibition exhibition = exhibitionRepository.findByExhibitionId(exhibitionId)
                .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.EXHIBITION_NOT_FOUND));

        final Room room = Room.builder()
                .title(roomCreateRoomRequest.getTitle())
                .description(roomCreateRoomRequest.getDescription())
                .thumbnail(roomCreateRoomRequest.getThumbnail())
                .build();

        room.setExhibition(exhibition);

        final Room savedRoom = roomRepository.save(room);

        final List<String> roomPhotoIds = roomCreateRoomRequest.getPhotos();

        Set<Long> genreIds = new HashSet<>();

        exhibition.getExhibitionGenres().stream().forEach(exhibitionGenre -> {
            genreIds.add(exhibitionGenre.getGenre().getId());
        });

        roomPhotoIds.stream().forEach(roomPhotoId -> {

            final Photo photo = photoRepository.findByPhotoId(UUID.fromString(roomPhotoId))
                    .orElseThrow(() -> new PhotoException(PhotoErrorResult.PHOTO_NOT_FOUND));

            final RoomPhoto roomPhoto = RoomPhoto.builder()
                    .photo(photo)
                    .build();

            roomPhoto.setRoom(savedRoom);

            roomPhotoRepository.save(roomPhoto);

            //add genreID in PhotoGenres into Set<String> genreIds
            photo.getPhotoGenres().stream().forEach(photoGenre -> {
                genreIds.add(photoGenre.getGenre().getId());
            });
        });
        //saveExhibitionGenres ( Set To List )

        saveExhibitionGenres(exhibition, (List<Long>) genreIds);

        final RoomDto.CreateRoomResponse roomCreateRoomResponseDto = RoomDto.CreateRoomResponse.builder()
                .roomId(savedRoom.getRoomId().toString())
                .createdAt(savedRoom.getCreatedAt().toString())
                .build();

        return roomCreateRoomResponseDto;
    }

    @Override
    @Transactional
    public RoomDto.DeleteRoomResponse deleteRoom(UUID roomId) {

        final Room room = roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.ROOM_NOT_FOUND));

        roomRepository.delete(room);

        final RoomDto.DeleteRoomResponse roomDeleteRoomResponseDto = RoomDto.DeleteRoomResponse.builder()
                .roomId(room.getRoomId().toString())
                .deletedAt(LocalDateTime.now().toString())
                .build();

        return roomDeleteRoomResponseDto;
    }

    @Override
    public RoomDto.ReadRoomListResponse readExhibitionRoomList(UUID exhibitionId) {

        final List<RoomDto> roomDtoList = roomRepository.findAllByExhibitionId(exhibitionId);

        final RoomDto.ReadRoomListResponse roomReadRoomListResponseDto = RoomDto.ReadRoomListResponse.builder()
                .rooms(roomDtoList)
                .build();

        return roomReadRoomListResponseDto;
    }

    public void saveExhibitionGenres(Exhibition savedExhibition, List<Long> genreIds) {
        genreIds.forEach(genreId -> {
            final Genre genre = genreRepository.findById(genreId)
                    .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.GENRE_NOT_FOUND));

            ExhibitionGenre exhibitionGenre = ExhibitionGenre.builder()
                    .genre(genre)
                    .build();

            exhibitionGenre.setExhibition(savedExhibition);

            savedExhibition.addExhibitionGenre(exhibitionGenre);

        });
    }
}
