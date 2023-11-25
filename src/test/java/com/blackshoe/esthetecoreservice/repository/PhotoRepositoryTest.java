package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.PhotoLocation;
import com.blackshoe.esthetecoreservice.entity.PhotoUrl;
import com.blackshoe.esthetecoreservice.vo.PhotoLocationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PhotoRepositoryTest {

    @Autowired
    private PhotoRepository photoRepository;

    final PhotoLocation photoLocation = PhotoLocation.builder()
            .longitude(1.0)
            .latitude(1.0)
            .state("state")
            .city("city")
            .town("town")
            .build();

    final PhotoUrl photoUrl = PhotoUrl.builder()
            .cloudfrontUrl("url")
            .build();

    final Photo photo = Photo.builder()
            .title("title")
            .description("description")
            .time("time")
            .build();

    @Test
    public void assert_isNotNull() {
        assertThat(photoRepository).isNotNull();
    }

    @Test
    public void findTop10ByUserLocationGroupByState_returns_readRegionGroupResponse() {
        // given
        photo.setPhotoLocation(photoLocation);
        photo.setPhotoUrl(photoUrl);

        photoRepository.save(photo);

        PhotoLocationFilter photoLocationFilter = PhotoLocationFilter.builder()
                .latitude(1.0)
                .longitude(1.0)
                .radius(1.0)
                .build();

        // when
        Page<PhotoDto.ReadRegionGroupResponse> readPhotoRegionGroupResponse
                = photoRepository.findTop10ByUserLocationGroupByState(photoLocationFilter);

        // then
        assertThat(readPhotoRegionGroupResponse.getContent()).isNotNull();
        assertThat(readPhotoRegionGroupResponse.getContent().get(0).getState()).isEqualTo("state");
    }

    @Test
    public void findTop10ByUserLocationGroupByCity_returns_readRegionGroupResponse() {
        // given
        photo.setPhotoLocation(photoLocation);
        photo.setPhotoUrl(photoUrl);

        photoRepository.save(photo);

        PhotoLocationFilter photoLocationFilter = PhotoLocationFilter.builder()
                .latitude(1.0)
                .longitude(1.0)
                .radius(1.0)
                .build();

        // when
        Page<PhotoDto.ReadRegionGroupResponse> readPhotoRegionGroupResponse
                = photoRepository.findTop10ByUserLocationGroupByCity(photoLocationFilter);

        // then
        assertThat(readPhotoRegionGroupResponse.getContent()).isNotNull();
        assertThat(readPhotoRegionGroupResponse.getContent().get(0).getCity()).isEqualTo("city");
    }

    @Test
    public void findTop10ByUserLocationGroupByTown_returns_readRegionGroupResponse() {
        // given
        photo.setPhotoLocation(photoLocation);
        photo.setPhotoUrl(photoUrl);

        photoRepository.save(photo);

        PhotoLocationFilter photoLocationFilter = PhotoLocationFilter.builder()
                .latitude(1.0)
                .longitude(1.0)
                .radius(1.0)
                .build();

        // when
        Page<PhotoDto.ReadRegionGroupResponse> readPhotoRegionGroupResponse
                = photoRepository.findTop10ByUserLocationGroupByTown(photoLocationFilter);

        // then
        assertThat(readPhotoRegionGroupResponse.getContent()).isNotNull();
        assertThat(readPhotoRegionGroupResponse.getContent().get(0).getTown()).isEqualTo("town");
    }
}
