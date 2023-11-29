package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.dto.GuestBookDto;
import com.blackshoe.esthetecoreservice.dto.SupportDto;
import com.blackshoe.esthetecoreservice.dto.UserDto;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.service.GuestBookService;
import com.blackshoe.esthetecoreservice.service.SupportService;
import com.blackshoe.esthetecoreservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private GuestBookService guestBookService;

    @MockBean
    private SupportService supportService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Logger log = LoggerFactory.getLogger(ExhibitionControllerTest.class);

    @Test
    public void readBasicInfo_whenSuccess_returns200() throws Exception {
        // given
        final UserDto.ReadBasicInfoResponse userReadBasicInfoResponse = UserDto.ReadBasicInfoResponse.builder()
                .userId("userId")
                .nickname("nickname")
                .profileImg("profileImg")
                .build();
        when(userService.readBasicInfo(any(UUID.class))).thenReturn(userReadBasicInfoResponse);

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        get("/core/users/{userId}/basic-info", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(userReadBasicInfoResponse));
    }

    @Test
    public void readBasicInfo_whenUserNotFound_returns404() throws Exception {
        // given
        when(userService.readBasicInfo(any(UUID.class))).thenThrow(new UserException(UserErrorResult.USER_NOT_FOUND));

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        get("/core/users/{userId}/basic-info", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        log.info("response: {}", response.getContentAsString());
    }

    @Test
    public void readCurrentExhibitionOfUser_whenSuccess_returns200() throws Exception {
        // given
        final ExhibitionDto.ReadCurrentOfUserExhibitionResponse exhibitionReadCurrentOfUserExhibitionResponse = ExhibitionDto.ReadCurrentOfUserExhibitionResponse.builder()
                .exhibitionId(UUID.randomUUID().toString())
                .title("title")
                .description("description")
                .thumbnail("thumbnail")
                .build();

        when(userService.readCurrentExhibitionOfUser(any(UUID.class))).thenReturn(exhibitionReadCurrentOfUserExhibitionResponse);

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        get("/core/users/{userId}/exhibitions/current", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(exhibitionReadCurrentOfUserExhibitionResponse));
    }

    @Test
    public void createGuestBook_whenSuccess_returns200() throws Exception {
        // given
        final GuestBookDto.CreateGuestBookRequest guestBookCreateGuestBookRequest = GuestBookDto.CreateGuestBookRequest.builder()
                .userId(UUID.randomUUID().toString())
                .content("content")
                .build();
        final GuestBookDto.CreateGuestBookResponse guestBookCreateGuestBookResponse = GuestBookDto.CreateGuestBookResponse.builder()
                .guestBookId(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now().toString())
                .build();
        when(guestBookService.createGuestBook(any(UUID.class), any(GuestBookDto.CreateGuestBookRequest.class))).thenReturn(guestBookCreateGuestBookResponse);

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        post("/core/users/{photographerId}/guest-books", UUID.randomUUID().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(guestBookCreateGuestBookRequest))
                ).andExpect(status().isCreated())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(guestBookCreateGuestBookResponse));
    }

    @Test
    public void createGuestBook_whenInvalidParma_returns400() throws Exception {
        // given
        String content = "";
        for (int i = 0; i < 102; i++) {
            content += "a";
        }

        final GuestBookDto.CreateGuestBookRequest guestBookCreateGuestBookRequest = GuestBookDto.CreateGuestBookRequest.builder()
                .userId(UUID.randomUUID().toString())
                .content(content)
                .build();

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        post("/core/users/{photographerId}/guest-books", UUID.randomUUID().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(guestBookCreateGuestBookRequest))
                ).andExpect(status().isBadRequest())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("error");
        log.info("response: {}", response.getContentAsString());
    }

    @Test
    public void createSupport_whenSuccess_returns201() throws Exception {
        // given
        final SupportDto.CreateSupportRequest supportCreateSupportRequest = SupportDto.CreateSupportRequest.builder()
                .photographerId(UUID.randomUUID().toString())
                .build();

        final SupportDto.CreateSupportResponse supportCreateSupportResponse = SupportDto.CreateSupportResponse.builder()
                .supportId(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now().toString())
                .build();

        when(supportService.createSupport(any(UUID.class), any(SupportDto.CreateSupportRequest.class))).thenReturn(supportCreateSupportResponse);

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        post("/core/users/{userId}/supports", UUID.randomUUID().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(supportCreateSupportRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(supportCreateSupportResponse));
    }

    @Test
    public void deleteSupport_whenSuccess_returns200() throws Exception {
        // given
        final SupportDto.DeleteSupportResponse supportDeleteSupportResponse = SupportDto.DeleteSupportResponse.builder()
                .supportId(UUID.randomUUID().toString())
                .deletedAt(LocalDateTime.now().toString())
                .build();

        when(supportService.deleteSupport(any(UUID.class), any(UUID.class))).thenReturn(supportDeleteSupportResponse);

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        delete("/core/users/{userId}/supports/{photographerId}", UUID.randomUUID().toString(), UUID.randomUUID().toString())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(supportDeleteSupportResponse));
    }

    @Test
    public void readAllNicknameContaining_whenSuccess_returns200() throws Exception {
        // given
        when(userService.readAllNicknameContaining(any(), any())).thenReturn(Page.empty());

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        get("/core/users/search")
                                .param("nickname", "nickname")
                                .param("sort", "recent")
                                .param("page", "0")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void readAllGenresContaining_whenSuccess_returns200() throws Exception {
        // given
        when(userService.readAllNicknameContaining(any(), any())).thenReturn(Page.empty());
        String genres = UUID.randomUUID() + "," + UUID.randomUUID();

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        get("/core/users/search")
                                .param("genres", genres)
                                .param("sort", "popular")
                                .param("page", "0")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void readAllNicknameAndGenresContaining_whenSuccess_returns200() throws Exception {
        // given
        when(userService.readAllNicknameContaining(any(), any())).thenReturn(Page.empty());
        String genres = UUID.randomUUID() + "," + UUID.randomUUID();

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        get("/core/users/search")
                                .param("nickname", "nickname")
                                .param("genres", genres)
                                .param("sort", "trending")
                                .param("page", "0")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void readAllNicknameAndGenresContaining_whenInvalidParam_returns400() throws Exception {
        // given
        when(userService.readAllNicknameContaining(any(), any())).thenReturn(Page.empty());
        String genres = "12345";

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        get("/core/users/search")
                                .param("nickname", "nickname")
                                .param("genres", genres)
                                .param("sort", "trending")
                                .param("page", "0")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void readAllNicknameAndGenresContaining_whenInvalidSortParam_returns400() throws Exception {
        // given
        when(userService.readAllNicknameContaining(any(), any())).thenReturn(Page.empty());
        String genres = UUID.randomUUID() + "," + UUID.randomUUID();

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        get("/core/users/search")
                                .param("nickname", "nickname")
                                .param("genres", genres)
                                .param("sort", "invalid")
                                .param("page", "0")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
