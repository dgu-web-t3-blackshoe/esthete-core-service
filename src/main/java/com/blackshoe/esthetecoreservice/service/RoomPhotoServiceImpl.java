package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.RoomPhotoDto;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.repository.RoomPhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomPhotoServiceImpl implements RoomPhotoService {

    private final RoomPhotoRepository roomPhotoRepository;

    @Override
    public RoomPhotoDto.ReadRoomPhotoListResponse readRoomPhotoList(UUID roomId) {

        final List<RoomPhotoDto> roomPhotos = roomPhotoRepository.findAllByRoomId(roomId);

        if (roomPhotos.isEmpty()) {
            throw new ExhibitionException(ExhibitionErrorResult.ROOM_PHOTO_NOT_FOUND);
        }

        final RoomPhotoDto.ReadRoomPhotoListResponse roomPhotoReadRoomPhotoListResponse
                = RoomPhotoDto.ReadRoomPhotoListResponse.builder()
                .roomPhotos(roomPhotos)
                .build();

        return roomPhotoReadRoomPhotoListResponse;
    }
}
