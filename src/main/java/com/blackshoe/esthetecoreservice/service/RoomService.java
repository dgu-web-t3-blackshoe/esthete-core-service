package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.RoomDto;

import java.util.UUID;

public interface RoomService {
    RoomDto.CreateResponse createRoom(RoomDto.CreateRequest roomCreateRequest, UUID exhibitionId);

    RoomDto.DeleteResponse deleteRoom(UUID roomId);

    RoomDto.ReadListResponse readExhibitionRoomList(UUID exhibitionId);
}
