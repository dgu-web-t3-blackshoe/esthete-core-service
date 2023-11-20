package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.entity.*;
import com.blackshoe.esthetecoreservice.repository.PhotoRepository;
import com.blackshoe.esthetecoreservice.service.PhotoService;
import com.blackshoe.esthetecoreservice.service.PhotoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PhotoController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
public class PhotoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PhotoController photoController;

    @MockBean
    PhotoService photoService;

    @Mock
    PhotoRepository photoRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Logger log = LoggerFactory.getLogger(ExhibitionControllerTest.class);
/*
    @Test
    public void getPhoto_whenSuccess_return200() throws Exception {
        log.info("getPhoto()");

        // given
        final UUID photoId = UUID.randomUUID();

        PhotoUrl photoUrl = PhotoUrl.builder()
                .s3Url("s3Url")
                .cloudfrontUrl("cloudfrontUrl")
                .build();

        PhotoLocation photoLocation = PhotoLocation.builder()
                .longitude(1.0)
                .latitude(1.0)
                .state("state")
                .city("city")
                .town("town")
                .build();

        PhotoEquipment equipment = PhotoEquipment.builder()
                .equipmentId(UUID.randomUUID())
                .build();

        Genre genre = Genre.builder()
                .genreId(UUID.randomUUID())
                .build();

        Photo photo = Photo.builder()
                .photoId(photoId)
                .title("title")
                .description("description")
                .time(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .photoUrl(photoUrl)
                .photoLocation(photoLocation)
                .photoEquipments(
                        List.of(equipment)
                )
                .genres(
                        List.of(genre)
                )
                .build();

        photoRepository.save(photo);

        PhotoDto.GetResponse photoGetResponse = PhotoDto.GetResponse.builder()
                .photoId(photoId.toString())
                //.userId(UUID.randomUUID().toString())
                .title("title")
                .description("description")
                .time(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .photoUrl(photoUrl)
                .photoLocation(photoLocation)
                .equipments(
                        List.of(equipment)
                )
                .genres(
                        List.of(genre)
                )
                .build();

        // when
        final MvcResult mvcResult = mockMvc.perform(
                get("/core/photos/{photoId}", photoId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(photoGetResponse));

    }
*/
    @Test
    public void getPhoto_whenPhotoNotFound_return404() throws Exception {
        log.info("getPhoto_whenPhotoNotFound_return404()");

        // given
        final UUID photoId = UUID.randomUUID();

        // when
        final MvcResult mvcResult = mockMvc.perform(
                get("/photos/{photoId}", photoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        // then
        final MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
