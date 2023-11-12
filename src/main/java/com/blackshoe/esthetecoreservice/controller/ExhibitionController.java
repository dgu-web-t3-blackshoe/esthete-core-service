package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.dto.RoomDto;
import com.blackshoe.esthetecoreservice.service.ExhibitionService;
import com.blackshoe.esthetecoreservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/exhibitions")
@RequiredArgsConstructor
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<ExhibitionDto.CreateResponse> createExhibition(@RequestBody @Valid ExhibitionDto.CreateRequest exhibitionCreateRequest) {

        final ExhibitionDto.CreateResponse exhibitionCreateResponse = exhibitionService.createExhibition(exhibitionCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(exhibitionCreateResponse);
    }

    @DeleteMapping("/{exhibitionId}")
    public ResponseEntity<ExhibitionDto.DeleteResponse> deleteExhibition(@PathVariable UUID exhibitionId) {

        final ExhibitionDto.DeleteResponse exhibitionDeleteResponse = exhibitionService.deleteExhibition(exhibitionId);

        return ResponseEntity.status(HttpStatus.OK).body(exhibitionDeleteResponse);
    }

    @PostMapping("/{exhibitionId}/rooms")
    public ResponseEntity<RoomDto.CreateResponse> createRoom(@RequestBody @Valid RoomDto.CreateRequest roomCreateRequest,
                                                             @PathVariable UUID exhibitionId) {

        final RoomDto.CreateResponse roomCreateResponse = roomService.createRoom(roomCreateRequest, exhibitionId);

        return ResponseEntity.status(HttpStatus.CREATED).body(roomCreateResponse);
    }

    @DeleteMapping("/{exhibitionId}/rooms/{roomId}")
    public ResponseEntity<RoomDto.DeleteResponse> deleteRoom(@PathVariable UUID exhibitionId, @PathVariable UUID roomId) {

        final RoomDto.DeleteResponse roomDeleteResponse = roomService.deleteRoom(roomId);

        return ResponseEntity.status(HttpStatus.OK).body(roomDeleteResponse);
    }

    @GetMapping("/random")
    public ResponseEntity<ExhibitionDto.GetRandomResponse> getRandomExhibition() {

        final ExhibitionDto.GetRandomResponse exhibitionGetRandomResponse = exhibitionService.getRandomExhibition();

        return ResponseEntity.status(HttpStatus.OK).body(exhibitionGetRandomResponse);
    }
}
