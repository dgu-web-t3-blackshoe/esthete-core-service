package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.ErrorDto;
import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.service.ExhibitionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExhibitionController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
public class ExhibitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExhibitionService exhibitionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createExhibition_whenSuccess_returns200() throws Exception {
        // given
        final ExhibitionDto.CreateRequest exhibitonCreateRequest = ExhibitionDto.CreateRequest.builder()
                .title("title")
                .description("description")
                .thumbnail(UUID.randomUUID().toString())
                .build();

        final ExhibitionDto.CreateResponse exhibitionCreateResponse = ExhibitionDto.CreateResponse.builder()
                .exhibitionId(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now().toString())
                .build();

        when(exhibitionService.createExhibition(any(ExhibitionDto.CreateRequest.class))).thenReturn(exhibitionCreateResponse);

        // when
        final MvcResult mvcResult = mockMvc.perform(
                post("/exhibitions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(exhibitonCreateRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(exhibitionCreateResponse));
    }

    @Test
    public void createExhibition_whenInvalidParam_returns400() throws Exception {
        // given
        final ExhibitionDto.CreateRequest exhibitonCreateRequest = ExhibitionDto.CreateRequest.builder()
                .title("title")
                .description("description")
                .thumbnail("thumbnail")
                .build();

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        post("/exhibitions")
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
        final ExhibitionDto.DeleteResponse exhibitionDeleteResponse = ExhibitionDto.DeleteResponse.builder()
                .exhibitionId(UUID.randomUUID().toString())
                .deletedAt(LocalDateTime.now().toString())
                .build();

        when(exhibitionService.deleteExhibition(any(UUID.class))).thenReturn(exhibitionDeleteResponse);

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        delete("/exhibitions/{exhibitionId}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(exhibitionDeleteResponse));
    }

    @Test
    public void deleteExhibition_whenPostNotFound_returns404() throws Exception {
        // given
        when(exhibitionService.deleteExhibition(any(UUID.class)))
                .thenThrow(new ExhibitionException(ExhibitionErrorResult.EXHIBITION_NOT_FOUND));

        // when
        final MvcResult mvcResult = mockMvc.perform(
                        delete("/exhibitions/{exhibitionId}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("error");
    }
}
