package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.RoomPhotoDto;

import java.util.UUID;

public interface RoomPhotoService {
    RoomPhotoDto.ReadListResponse readRoomPhotoList(UUID roomId);
}
