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

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final ExhibitionRepository exhibitionRepository;
    private final RoomRepository roomRepository;
    private final PhotoRepository photoRepository;
    private final RoomPhotoRepository roomPhotoRepository;
    private final GenreRepository genreRepository;
    private final ExhibitionGenreRepository exhibitionGenreRepository;
    private final PhotoGenreRepository photoGenreRepository;
    private final EntityManager entityManager;

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


        Set<Long> exhibitionGenreIds = exhibition.getExhibitionGenres().stream()
                .map(exhibitionGenre ->
                        exhibitionGenre.getGenre().getId()).collect(Collectors.toSet());


        roomPhotoIds.stream().forEach(roomPhotoId -> {
            final Photo photo = photoRepository.findByPhotoId(UUID.fromString(roomPhotoId))
                    .orElseThrow(() -> new PhotoException(PhotoErrorResult.PHOTO_NOT_FOUND));

            final RoomPhoto roomPhoto = RoomPhoto.builder()
                    .photo(photo)
                    .build();

            roomPhoto.setRoom(savedRoom);

            roomPhotoRepository.save(roomPhoto);

            List<PhotoGenre> photoGenres = photoGenreRepository.findByPhoto(photo).orElseThrow(() -> new PhotoException(PhotoErrorResult.PHOTO_GENRE_NOT_FOUND));

            photoGenres.stream().forEach(photoGenre -> {
                exhibitionGenreIds.add(photoGenre.getGenre().getId());
            });
        });

        exhibitionGenreIds.stream().forEach(exhibitionGenreId -> {
            final Genre genre = genreRepository.findById(exhibitionGenreId)
                    .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.GENRE_NOT_FOUND));

            // 존재하는지 확인
            boolean exists = exhibitionGenreRepository.findByExhibitionIdAndGenreId(exhibition.getId(), genre.getId()).isPresent();

            // 존재하지 않는 경우에만 추가
            if (!exists) {
                ExhibitionGenre exhibitionGenre = ExhibitionGenre.builder()
                        .genre(genre)
                        .build();

                exhibitionGenre.setExhibition(exhibition);
                exhibitionGenreRepository.save(exhibitionGenre);
            }
        });

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

        roomDtoList.stream().forEach(roomDto -> {
            final String thumbnail = roomDto.getThumbnail();
            final Photo photo = photoRepository.findByPhotoId(UUID.fromString(thumbnail))
                    .orElseThrow(() -> new PhotoException(PhotoErrorResult.PHOTO_NOT_FOUND));

            roomDto.setThumbnail(photo.getPhotoUrl().getCloudfrontUrl());
        });

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
