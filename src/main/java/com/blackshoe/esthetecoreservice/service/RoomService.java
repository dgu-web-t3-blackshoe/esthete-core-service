package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.RoomDto;

import java.util.UUID;

public interface RoomService {
    RoomDto.CreateRoomResponse createRoom(RoomDto.CreateRoomRequest roomCreateRoomRequest, UUID exhibitionId);

    RoomDto.DeleteRoomResponse deleteRoom(UUID roomId);

    RoomDto.ReadRoomListResponse readExhibitionRoomList(UUID exhibitionId);
}
