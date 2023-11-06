package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;

import static org.assertj.core.api.Assertions.assertThat;

@EntityListeners(AuditingEntityListener.class)
@DataJpaTest
public class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void assert_isNotNull() {
        assertThat(roomRepository).isNotNull();
    }

    @Test
    public void save_returns_savedRoom() {
        // given
        final Room room = Room.builder()
                .title("title")
                .description("description")
                .thumbnail("thumbnail")
                .build();

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
}
