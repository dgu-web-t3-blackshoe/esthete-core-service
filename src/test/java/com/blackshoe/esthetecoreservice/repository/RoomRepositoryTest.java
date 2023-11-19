package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.dto.RoomDto;
import com.blackshoe.esthetecoreservice.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@EntityListeners(AuditingEntityListener.class)
@DataJpaTest
public class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomPhotoRepository roomPhotoRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    private final Room room = Room.builder()
            .title("title")
            .description("description")
            .thumbnail("thumbnail")
            .build();

    private final PhotoUrl photoUrl = PhotoUrl.builder()
            .cloudfrontUrl("cloudfrontUrl")
            .s3Url("s3Url")
            .build();

    private final Photo photo = Photo.builder()
            .title("title")
            .description("description")
            .time(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
            .build();

    @Test
    public void assert_isNotNull() {
        assertThat(roomRepository).isNotNull();
    }

    @Test
    public void save_returns_savedRoom() {
        // given

        // when
        final Room savedRoom = roomRepository.save(room);

        // then
        assertThat(savedRoom).isNotNull();
        assertThat(savedRoom.getId()).isNotNull();
        assertThat(savedRoom.getRoomId()).isNotNull();
        assertThat(savedRoom.getCreatedAt()).isNotNull();
        assertThat(savedRoom.getTitle()).isEqualTo(room.getTitle());
        assertThat(savedRoom.getDescription()).isEqualTo(room.getDescription());
        assertThat(savedRoom.getThumbnail()).isEqualTo(room.getThumbnail());
    }

    @Test
    public void addRoomPhoto_returns_addedRoomPhoto() {
        // given
        final Room savedRoom = roomRepository.save(room);

        photo.setPhotoUrl(photoUrl);
        final Photo savedPhoto = photoRepository.save(photo);

        final RoomPhoto roomPhoto = RoomPhoto.builder()
                .room(savedRoom)
                .photo(savedPhoto)
                .build();

        final RoomPhoto savedRoomPhoto =  roomPhotoRepository.save(roomPhoto);

        // when
        savedRoom.addRoomPhoto(savedRoomPhoto);
    }

    @Test
    public void deleteRoom_onSuccess_alsoDeleteRoomPhoto() {
        // given
        final Room savedRoom = roomRepository.save(room);

        photo.setPhotoUrl(photoUrl);
        final Photo savedPhoto = photoRepository.save(photo);

        final RoomPhoto roomPhoto = RoomPhoto.builder()
                .room(savedRoom)
                .photo(savedPhoto)
                .build();

        final RoomPhoto savedRoomPhoto =  roomPhotoRepository.save(roomPhoto);

        savedRoom.addRoomPhoto(savedRoomPhoto);

        // when
        roomRepository.delete(savedRoom);

        // then
        assertThat(roomRepository.findById(savedRoom.getId())).isEmpty();
        assertThat(roomPhotoRepository.findById(savedRoomPhoto.getId())).isEmpty();
        assertThat(photoRepository.findById(savedPhoto.getId())).isNotEmpty();
    }

    @Test
    public void findByRoomId_returns_room() {
        // given
        final Room savedRoom = roomRepository.save(room);

        // when
        final Room foundRoom = roomRepository.findByRoomId(savedRoom.getRoomId()).orElse(null);

        // then
        assertThat(foundRoom).isNotNull();
        assertThat(foundRoom.getId()).isEqualTo(savedRoom.getId());
        assertThat(foundRoom.getRoomId()).isEqualTo(savedRoom.getRoomId());
        assertThat(foundRoom.getCreatedAt()).isEqualTo(savedRoom.getCreatedAt());
        assertThat(foundRoom.getTitle()).isEqualTo(savedRoom.getTitle());
        assertThat(foundRoom.getDescription()).isEqualTo(savedRoom.getDescription());
        assertThat(foundRoom.getThumbnail()).isEqualTo(savedRoom.getThumbnail());
    }

    @Test
    public void findAllByExhibitionId_returns_roomList() {
        // given
        final Exhibition exhibition = Exhibition.builder()
                .title("title")
                .description("description")
                .thumbnail("thumbnail")
                .build();

        final Exhibition savedExhibition = exhibitionRepository.save(exhibition);

        final UUID exhibitionId = savedExhibition.getExhibitionId();

        final Room room1 = Room.builder()
                .title("1")
                .description("description")
                .thumbnail("thumbnail")
                .build();

        room1.setExhibition(savedExhibition);

        final Room room2 = Room.builder()
                .title("2")
                .description("description")
                .thumbnail("thumbnail")
                .build();

        room2.setExhibition(savedExhibition);

        final Room room3 = Room.builder()
                .title("3")
                .description("description")
                .thumbnail("thumbnail")
                .build();

        room3.setExhibition(savedExhibition);

        // when
        final List<RoomDto> rooms = roomRepository.findAllByExhibitionId(exhibitionId);

        // then
        assertThat(rooms).isNotNull();
        assertThat(rooms.size()).isEqualTo(3);
        IntStream.range(0, rooms.size() - 1)
                .forEach(i -> assertThat(rooms.get(i).getTitle()).isLessThan(rooms.get(i + 1).getTitle()));
    }
}
