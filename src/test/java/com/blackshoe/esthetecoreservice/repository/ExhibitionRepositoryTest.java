package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;

import static org.assertj.core.api.Assertions.assertThat;

@EntityListeners(AuditingEntityListener.class)
@DataJpaTest
public class ExhibitionRepositoryTest {

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Autowired
    private RoomRepository roomRepository;

    private final Exhibition exhibition = Exhibition.builder()
            .title("title")
            .description("description")
            .thumbnail("thumbnail")
            .build();

    @Test
    public void assert_isNotNull() {
        assertThat(exhibitionRepository).isNotNull();
    }

    @Test
    public void save_returns_savedExhibition() {
        // given

        // when
        final Exhibition savedExhibition = exhibitionRepository.save(exhibition);

        // then
        assertThat(savedExhibition).isNotNull();
        assertThat(savedExhibition.getId()).isNotNull();
        assertThat(savedExhibition.getExhibitionId()).isNotNull();
        assertThat(savedExhibition.getCreatedAt()).isNotNull();
        assertThat(savedExhibition.getTitle()).isEqualTo(exhibition.getTitle());
        assertThat(savedExhibition.getDescription()).isEqualTo(exhibition.getDescription());
        assertThat(savedExhibition.getThumbnail()).isEqualTo(exhibition.getThumbnail());
    }

    @Test
    public void addRoom_returns_addedRoom() {
        // given
        final Exhibition savedExhibition = exhibitionRepository.save(exhibition);

        final Room room = Room.builder()
                .title("title")
                .description("description")
                .thumbnail("thumbnail")
                .exhibition(savedExhibition)
                .build();

        final Room savedRoom = roomRepository.save(room);

        // when
        savedExhibition.addRoom(savedRoom);

        // then
        assertThat(savedExhibition.getRooms().get(0)).isEqualTo(savedRoom);
    }

    @Test
    public void deleteExhibition_onSuccess_alsoDeleteRoom() {
        // given
        final Exhibition savedExhibition = exhibitionRepository.save(exhibition);

        final Room room = Room.builder()
                .title("title")
                .description("description")
                .thumbnail("thumbnail")
                .exhibition(savedExhibition)
                .build();

        final Room savedRoom = roomRepository.save(room);

        savedExhibition.addRoom(savedRoom);

        // when
        exhibitionRepository.delete(savedExhibition);

        // then
        assertThat(exhibitionRepository.findById(savedExhibition.getId())).isEmpty();
        assertThat(roomRepository.findById(savedRoom.getId())).isEmpty();
    }
}