package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.dto.RoomDto;
import com.blackshoe.esthetecoreservice.dto.RoomPhotoDto;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.service.ExhibitionService;
import com.blackshoe.esthetecoreservice.service.RoomPhotoService;
import com.blackshoe.esthetecoreservice.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExhibitionController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
public class ExhibitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExhibitionService exhibitionService;

    @MockBean
    private RoomService roomService;

    @MockBean
    private RoomPhotoService roomPhotoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Logger log = LoggerFactory.getLogger(ExhibitionControllerTest.class);

    @Test
    public void createExhibition_whenSuccess_returns201() throws Exception {
        // given
        final ExhibitionDto.CreateExhibitionRequest exhibitonCreateRequest = ExhibitionDto.CreateExhibitionRequest.builder()
                .title("title")
                .description("description")
                .thumbnail(UUID.randomUUID().toString())
                .build();

        final ExhibitionDto.CreateExhibitionResponse exhibitionCreateExhibitionResponse = ExhibitionDto.CreateExhibitionResponse.builder()
                .exhibitionId(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now().toString())
                .build();

        when(exhibitionService.createExhibition(any(ExhibitionDto.CreateExhibitionRequest.class))).thenReturn(exhibitionCreateExhibitionResponse);

        // when
        final MvcResult mvcResult = mockMvc.perform(
                post("/core/exhibitions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(exhibitonCreateRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(exhibitionCreateExhibitionResponse));
    }

    @Test
    public void createExhibition_whenInvalidParam_returns400() throws Exception {
        // given
        final ExhibitionDto.CreateExhibitionRequest exhibitonCreateRequest = ExhibitionDto.CreateExhibitionRequest.builder()
                .title("title")
                .description("description")
                .thumbnail("thumbnail")
                .build();

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        post("/core/exhibitions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(exhibitonCreateRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("error");
    }

    @Test
    public void deleteExhibition_whenSuccess_returns200() throws Exception {
        // given
        final ExhibitionDto.DeleteExhibitionResponse exhibitionDeleteExhibitionResponse = ExhibitionDto.DeleteExhibitionResponse.builder()
                .exhibitionId(UUID.randomUUID().toString())
                .deletedAt(LocalDateTime.now().toString())
                .build();

        when(exhibitionService.deleteExhibition(any(UUID.class))).thenReturn(exhibitionDeleteExhibitionResponse);

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        delete("/core/exhibitions/{exhibitionId}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(exhibitionDeleteExhibitionResponse));
    }

    @Test
    public void deleteExhibition_whenExhibitionNotFound_returns404() throws Exception {
        // given
        when(exhibitionService.deleteExhibition(any(UUID.class)))
                .thenThrow(new ExhibitionException(ExhibitionErrorResult.EXHIBITION_NOT_FOUND));

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        delete("/core/exhibitions/{exhibitionId}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("error");
        log.info("response: {}", response.getContentAsString());
    }

    @Test
    public void createRoom_whenSuccess_returns201() throws Exception {
        // given
        final RoomDto.CreateRoomRequest roomCreateRoomRequest = RoomDto.CreateRoomRequest.builder()
                .title("title")
                .description("description")
                .thumbnail(UUID.randomUUID().toString())
                .photos(List.of(UUID.randomUUID().toString()))
                .build();

        final RoomDto.CreateRoomResponse roomCreateRoomResponse = RoomDto.CreateRoomResponse.builder()
                .roomId(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now().toString())
                .build();

        when(roomService.createRoom(any(RoomDto.CreateRoomRequest.class), any(UUID.class))).thenReturn(roomCreateRoomResponse);

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        post("/core/exhibitions/{exhibitionId}/rooms", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(roomCreateRoomRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(roomCreateRoomResponse));
    }

    @Test
    public void createRoom_whenInvalidParam_returns400() throws Exception {
        // given
        final RoomDto.CreateRoomRequest roomCreateRoomRequest = RoomDto.CreateRoomRequest.builder()
                .title("title")
                .description("description")
                .thumbnail(UUID.randomUUID().toString())
                .photos(List.of("photo"))
                .build();

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        post("/core/exhibitions/{exhibitionId}/rooms", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(roomCreateRoomRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("error");
        log.info("response: {}", response.getContentAsString());
    }

    @Test
    public void deleteRoom_whenSuccess_returns200() throws Exception {
        // given
        final RoomDto.DeleteRoomResponse roomDeleteRoomResponse = RoomDto.DeleteRoomResponse.builder()
                .roomId(UUID.randomUUID().toString())
                .deletedAt(LocalDateTime.now().toString())
                .build();

        when(roomService.deleteRoom(any(UUID.class))).thenReturn(roomDeleteRoomResponse);

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        delete("/core/exhibitions/{exhibitionId}/rooms/{roomId}", UUID.randomUUID(), UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(roomDeleteRoomResponse));
    }

    @Test
    public void deleteRoom_whenRoomNotFound_returns404() throws Exception {
        // given
        when(roomService.deleteRoom(any(UUID.class)))
                .thenThrow(new ExhibitionException(ExhibitionErrorResult.ROOM_NOT_FOUND));

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        delete("/core/exhibitions/{exhibitionId}/rooms/{roomId}", UUID.randomUUID(), UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("error");
        log.info("response: {}", response.getContentAsString());
    }

    @Test
    public void readRandomExhibition_whenSuccess_returns200() throws Exception {
        // given
        final ExhibitionDto.ReadRandomExhibitionResponse exhibitionReadRandomExhibitionResponse = ExhibitionDto.ReadRandomExhibitionResponse.builder()
                .exhibitionId(UUID.randomUUID().toString())
                .title("title")
                .description("description")
                .thumbnail("thumbnail")
                .userId(UUID.randomUUID().toString())
                .nickname("nickname")
                .profileImg("profileImg")
                .build();

        when(exhibitionService.readRandomExhibition()).thenReturn(exhibitionReadRandomExhibitionResponse);

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        get("/core/exhibitions/random")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(exhibitionReadRandomExhibitionResponse));
    }

    @Test
    public void readExhibitionRoomList_whenSuccess_returns200() throws Exception {
        // given
        final RoomDto roomDto = RoomDto.builder()
                .roomId(UUID.randomUUID().toString())
                .title("title")
                .description("description")
                .thumbnail("thumbnail")
                .build();

        final RoomDto.ReadRoomListResponse roomReadRoomListResponse = RoomDto.ReadRoomListResponse.builder()
                .rooms(List.of(roomDto))
                .build();

        when(roomService.readExhibitionRoomList(any(UUID.class))).thenReturn(roomReadRoomListResponse);

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        get("/core/exhibitions/{exhibitionId}/rooms", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(roomReadRoomListResponse));
    }

    @Test
    public void readExhibitionRoomPhotoList_whenSuccess_returns200() throws Exception {
        // given
        final RoomPhotoDto.ReadRoomPhotoListResponse roomPhotoReadRoomPhotoListResponse = RoomPhotoDto.ReadRoomPhotoListResponse.builder()
                .roomPhotos(List.of())
                .build();

        when(roomPhotoService.readRoomPhotoList(any(UUID.class))).thenReturn(roomPhotoReadRoomPhotoListResponse);

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        get("/core/exhibitions/{exhibitionId}/rooms/{roomId}", UUID.randomUUID(), UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(roomPhotoReadRoomPhotoListResponse));
    }
}
