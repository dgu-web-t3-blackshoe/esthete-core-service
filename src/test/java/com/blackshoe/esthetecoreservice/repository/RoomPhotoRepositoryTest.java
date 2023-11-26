package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.dto.RoomPhotoDto;
import com.blackshoe.esthetecoreservice.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private UserRepository userRepository;

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

    private final User user = User.builder()
            .biography("biography")
            .nickname("nickname")
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

    @Test
    public void findAllByRoomId_returns_RoomPhotoDtoList() {
        // given
        final Room savedRoom = roomRepository.save(room);

        final UUID roomId = savedRoom.getRoomId();

        final User savedUser = userRepository.save(user);

        for (int i = 0; i < 10; i++) {
            final PhotoUrl savedPhotoUrl = photoUrlRepository.save(photoUrl);

            photo.setPhotoUrl(savedPhotoUrl);

            photo.setUser(savedUser);

            final Photo savedPhoto = photoRepository.save(photo);

            final RoomPhoto roomPhoto = RoomPhoto.builder()
                    .room(savedRoom)
                    .photo(savedPhoto)
                    .build();

            roomPhotoRepository.save(roomPhoto);
        }

        // when
        final List<RoomPhotoDto> roomPhotoDtoList = roomPhotoRepository.findAllByRoomId(roomId);

        // then
        assertThat(roomPhotoDtoList).isNotNull();
        assertThat(roomPhotoDtoList.size()).isEqualTo(10);
        assertThat(roomPhotoDtoList.get(0).getRoomId()).isEqualTo(roomId.toString());
        assertThat(roomPhotoDtoList.get(0).getPhotoId()).isNotNull();
        assertThat(roomPhotoDtoList.get(0).getTitle()).isEqualTo("title");
        assertThat(roomPhotoDtoList.get(0).getPhoto()).isEqualTo("cloudfrontUrl");
        assertThat(roomPhotoDtoList.get(0).getUserId()).isNotNull();
        assertThat(roomPhotoDtoList.get(0).getNickname()).isEqualTo("nickname");
    }

    @Test
    public void findAllByRoomId_whenEmptyRoom_returnsEmptyList() {
        // given
        final Room savedRoom = roomRepository.save(room);

        final UUID roomId = savedRoom.getRoomId();

        // when
        final List<RoomPhotoDto> roomPhotoDtoList = roomPhotoRepository.findAllByRoomId(roomId);

        // then
        assertThat(roomPhotoDtoList).isNotNull();
        assertThat(roomPhotoDtoList.size()).isEqualTo(0);
    }
}
