package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;

import static org.assertj.core.api.Assertions.assertThat;

@EntityListeners(AuditingEntityListener.class)
@DataJpaTest
public class RoomPhotoRepositoryTest {

    @Autowired
    private RoomPhotoRepository roomPhotoRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoUrlRepository photoUrlRepository;

    private final PhotoUrl photoUrl = PhotoUrl.builder()
            .cloudfrontUrl("cloudfrontUrl")
            .s3Url("s3Url")
            .build();

    private final Photo photo = Photo.builder()
            .title("title")
            .description("description")
            .time("time")
            .build();

    private final Room room = Room.builder()
            .title("title")
            .description("description")
            .thumbnail("thumbnail")
            .build();

    @Test
    public void assert_isNotNull() {
        assertThat(roomPhotoRepository).isNotNull();
    }

    @Test
    public void save_returns_savedRoomPhoto() {
        // given
        final PhotoUrl savedPhotoUrl = photoUrlRepository.save(photoUrl);

        photo.setPhotoUrl(savedPhotoUrl);

        final Photo savedPhoto = photoRepository.save(photo);

        final Room savedRoom = roomRepository.save(room);

        final RoomPhoto roomPhoto = RoomPhoto.builder()
                .room(savedRoom)
                .photo(savedPhoto)
                .build();

        // when
        final RoomPhoto savedRoomPhoto = roomPhotoRepository.save(roomPhoto);

        // then
        assertThat(savedRoomPhoto).isNotNull();
        assertThat(savedRoomPhoto.getId()).isNotNull();
        assertThat(savedRoomPhoto.getRoomPhotoId()).isNotNull();
        assertThat(savedRoomPhoto.getCreatedAt()).isNotNull();
        assertThat(savedRoomPhoto.getRoom()).isEqualTo(room);
        assertThat(savedRoomPhoto.getPhoto()).isEqualTo(photo);
    }
}
