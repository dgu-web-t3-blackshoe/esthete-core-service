package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.PhotoChecksum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PhotoChecksumRepositoryTest {

    @Autowired
    private PhotoChecksumRepository photoChecksumRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Test
    public void assert_isNotNull() {
        assert photoChecksumRepository != null;
        assert photoRepository != null;
    }

    @Test
    public void photoSave_also_savePhotoChecksum() {
        // given
        final Photo photo = Photo.builder()
                .build();

        final PhotoChecksum photoChecksum = PhotoChecksum.builder()
                .checksum("checksum")
                .build();

        photo.setPhotoChecksum(photoChecksum);

        // when
        photoRepository.save(photo);

        // then
        assertThat(photoChecksum.getId()).isNotNull();
        assertThat(photoChecksum.getId()).isEqualTo(1L);
    }

    @Test
    public void photoDelete_also_deletesPhotoChecksum() {
        // given
        final Photo photo = Photo.builder()
                .build();

        final PhotoChecksum photoChecksum = PhotoChecksum.builder()
                .checksum("checksum")
                .build();

        photo.setPhotoChecksum(photoChecksum);

        // when
        photoRepository.save(photo);
        photoRepository.delete(photo);

        // then
        assertThat(photoChecksumRepository.findById(1L)).isEmpty();
    }
}
