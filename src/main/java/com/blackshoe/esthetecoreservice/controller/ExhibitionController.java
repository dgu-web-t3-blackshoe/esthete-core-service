package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.dto.RoomDto;
import com.blackshoe.esthetecoreservice.dto.RoomPhotoDto;
import com.blackshoe.esthetecoreservice.service.ExhibitionService;
import com.blackshoe.esthetecoreservice.service.RecommendationService;
import com.blackshoe.esthetecoreservice.service.RoomPhotoService;
import com.blackshoe.esthetecoreservice.service.RoomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/core/exhibitions")
@RequiredArgsConstructor
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    private final RoomService roomService;

    private final RoomPhotoService roomPhotoService;


    @PostMapping
    public ResponseEntity<ExhibitionDto.CreateExhibitionResponse> createExhibition(@RequestBody @Valid ExhibitionDto.CreateExhibitionRequest exhibitionCreateRequest) throws JsonProcessingException{

        final ExhibitionDto.CreateExhibitionResponse exhibitionCreateExhibitionResponse = exhibitionService.createExhibition(exhibitionCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(exhibitionCreateExhibitionResponse);
    }

    @DeleteMapping("/{exhibitionId}")
    public ResponseEntity<ExhibitionDto.DeleteExhibitionResponse> deleteExhibition(@PathVariable UUID exhibitionId) {

        final ExhibitionDto.DeleteExhibitionResponse exhibitionDeleteExhibitionResponse = exhibitionService.deleteExhibition(exhibitionId);

        return ResponseEntity.status(HttpStatus.OK).body(exhibitionDeleteExhibitionResponse);
    }

    @PostMapping("/{exhibitionId}/rooms")
    public ResponseEntity<RoomDto.CreateRoomResponse> createRoom(@RequestBody @Valid RoomDto.CreateRoomRequest roomCreateRoomRequest,
                                                                 @PathVariable UUID exhibitionId) {

        final RoomDto.CreateRoomResponse roomCreateRoomResponse = roomService.createRoom(roomCreateRoomRequest, exhibitionId);

        return ResponseEntity.status(HttpStatus.CREATED).body(roomCreateRoomResponse);
    }

    @DeleteMapping("/{exhibitionId}/rooms/{roomId}")
    public ResponseEntity<RoomDto.DeleteRoomResponse> deleteRoom(@PathVariable UUID exhibitionId, @PathVariable UUID roomId) {

        final RoomDto.DeleteRoomResponse roomDeleteRoomResponse = roomService.deleteRoom(roomId);

        return ResponseEntity.status(HttpStatus.OK).body(roomDeleteRoomResponse);
    }

    @GetMapping("/random")
    public ResponseEntity<ExhibitionDto.ReadRandomExhibitionResponse> getRandomExhibition() {

        final ExhibitionDto.ReadRandomExhibitionResponse exhibitionReadRandomExhibitionResponse = exhibitionService.readRandomExhibition();

        return ResponseEntity.status(HttpStatus.OK).body(exhibitionReadRandomExhibitionResponse);
    }

    @GetMapping("/{exhibitionId}/rooms")
    public ResponseEntity<RoomDto.ReadRoomListResponse> getExhibitionRoomList(@PathVariable UUID exhibitionId) {

        final RoomDto.ReadRoomListResponse roomReadRoomListResponse = roomService.readExhibitionRoomList(exhibitionId);

        return ResponseEntity.status(HttpStatus.OK).body(roomReadRoomListResponse);
    }

    @GetMapping("/{exhibitionId}/rooms/{roomId}")
    public ResponseEntity<RoomPhotoDto.ReadRoomPhotoListResponse> getExhibitionRoomPhoto(@PathVariable UUID exhibitionId, @PathVariable UUID roomId) {

        final RoomPhotoDto.ReadRoomPhotoListResponse roomPhotoReadRoomPhotoListResponse = roomPhotoService.readRoomPhotoList(roomId);

        return ResponseEntity.status(HttpStatus.OK).body(roomPhotoReadRoomPhotoListResponse);
    }

}
