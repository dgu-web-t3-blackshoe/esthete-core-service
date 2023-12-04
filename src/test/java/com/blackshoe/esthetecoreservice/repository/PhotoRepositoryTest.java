package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.PhotoLocation;
import com.blackshoe.esthetecoreservice.entity.PhotoUrl;
import com.blackshoe.esthetecoreservice.vo.PhotoAddressFilter;
import com.blackshoe.esthetecoreservice.vo.PhotoPointFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EntityListeners(AuditingEntityListener.class)
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

        PhotoPointFilter photoPointFilter = PhotoPointFilter.builder()
                .latitude(1.0)
                .longitude(1.0)
                .radius(1.0)
                .build();

        // when
        Page<PhotoDto.ReadRegionGroupResponse> readPhotoRegionGroupResponse
                = photoRepository.findTop10ByUserLocationGroupByState(photoPointFilter);

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

        PhotoPointFilter photoPointFilter = PhotoPointFilter.builder()
                .latitude(1.0)
                .longitude(1.0)
                .radius(1.0)
                .build();

        // when
        Page<PhotoDto.ReadRegionGroupResponse> readPhotoRegionGroupResponse
                = photoRepository.findTop10ByUserLocationGroupByCity(photoPointFilter);

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

        PhotoPointFilter photoPointFilter = PhotoPointFilter.builder()
                .latitude(1.0)
                .longitude(1.0)
                .radius(1.0)
                .build();

        // when
        Page<PhotoDto.ReadRegionGroupResponse> readPhotoRegionGroupResponse
                = photoRepository.findTop10ByUserLocationGroupByTown(photoPointFilter);

        // then
        assertThat(readPhotoRegionGroupResponse.getContent()).isNotNull();
        assertThat(readPhotoRegionGroupResponse.getContent().get(0).getTown()).isEqualTo("town");
    }

    @Test
    public void findAllByPhotoLocationState_returns_readResponse() {
        // given
        photo.setPhotoLocation(photoLocation);
        photo.setPhotoUrl(photoUrl);

        photoRepository.save(photo);

        PhotoAddressFilter photoAddressFilter = PhotoAddressFilter.builder()
                .state("state")
                .build();

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(0, 10, sort);

        // when
        Page<PhotoDto.ReadPhotoResponse> readPhotoResponse
                = photoRepository.findAllByPhotoLocationState(photoAddressFilter, pageable);

        // then
        assertThat(readPhotoResponse.getContent()).isNotNull();
        assertThat(readPhotoResponse.getContent().get(0).getTitle()).isEqualTo(photo.getTitle());
    }

    @Test
    public void findAllByPhotoLocationStateAndCity_returns_readResponse() {
        // given
        photo.setPhotoLocation(photoLocation);
        photo.setPhotoUrl(photoUrl);

        photoRepository.save(photo);

        PhotoAddressFilter photoAddressFilter = PhotoAddressFilter.builder()
                .state("state")
                .city("city")
                .build();

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(0, 10, sort);

        // when
        Page<PhotoDto.ReadPhotoResponse> readPhotoResponse
                = photoRepository.findAllByPhotoLocationStateAndCity(photoAddressFilter, pageable);

        // then
        assertThat(readPhotoResponse.getContent()).isNotNull();
        assertThat(readPhotoResponse.getContent().get(0).getTitle()).isEqualTo(photo.getTitle());
    }

    @Test
    public void findAllByPhotoLocationStateAndCityAndTown_returns_readResponse() {
        // given
        photo.setPhotoLocation(photoLocation);
        photo.setPhotoUrl(photoUrl);

        photoRepository.save(photo);

        PhotoAddressFilter photoAddressFilter = PhotoAddressFilter.builder()
                .state("state")
                .city("city")
                .town("town")
                .build();

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(0, 10, sort);

        // when
        Page<PhotoDto.ReadPhotoResponse> readPhotoResponse
                = photoRepository.findAllByPhotoLocationStateAndCityAndTown(photoAddressFilter, pageable);

        // then
        assertThat(readPhotoResponse.getContent()).isNotNull();
        assertThat(readPhotoResponse.getContent().get(0).getTitle()).isEqualTo(photo.getTitle());
    }
}
