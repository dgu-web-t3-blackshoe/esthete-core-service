package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.entity.Equipment;
import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.PhotoLocation;
import com.blackshoe.esthetecoreservice.entity.PhotoUrl;
import com.blackshoe.esthetecoreservice.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhotoServiceTest {

    @InjectMocks
    private PhotoServiceImpl photoService;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private ViewRepository photoViewRepository;

    @Mock
    private PhotoGenreRepository photoGenreRepository;

    @Mock
    private PhotoUrlRepository photoUrlRepository;

    @Mock
    private PhotoLocationRepository photoLocationRepository;

    private final UUID photoId = UUID.randomUUID();
    private final PhotoUrl photoUrl = PhotoUrl.builder()
            .s3Url("s3Url")
            .cloudfrontUrl("cloudfrontUrl")
            .build();
    private final PhotoLocation photoLocation = PhotoLocation.builder()
            .longitude(1.0)
            .latitude(1.0)
            .state("state")
            .city("city")
            .town("town")
            .build();

    private final List<Equipment> equipments = List.of(
            //repository findById로 가져온 photo의 equipments
            Equipment.builder()
                    .equipmentId(UUID.randomUUID())
                    .build()
    );

    private final Photo photo = Photo.builder()
            .photoId(photoId)
            .title("title")
            .description("description")
            .time(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
            .photoUrl(photoUrl)
            .photoLocation(photoLocation)
            .equipments(equipments)
            .build();

    @Test
    void getPhoto_whenSuccess_returnGetPhotoResponse() {
        // given
        photoRepository.save(photo);
        when(photoRepository.findByPhotoId(photoId)).thenReturn(Optional.of(photo));
        long viewCount = photoViewRepository.countByPhotoId(photoId) - 1;

        // when
        PhotoDto.GetResponse getPhotoResponse = photoService.getPhoto(photoId);

        // then
        assertThat(getPhotoResponse.getTitle()).isEqualTo(photo.getTitle());
        assertThat(getPhotoResponse.getDescription()).isEqualTo(photo.getDescription());
        assertThat(getPhotoResponse.getTime()).isEqualTo(photo.getTime());
        assertThat(getPhotoResponse.getPhotoUrl()).isEqualTo(photo.getPhotoUrl());
        assertThat(getPhotoResponse.getPhotoLocation()).isEqualTo(photo.getPhotoLocation());
        assertThat(getPhotoResponse.getEquipments()).isEqualTo(photo.getEquipments());
        assertThat(getPhotoResponse.getViewCount()).isEqualTo(viewCount);
    }

}
