package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.Exhibition;
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

    @Test
    public void assert_isNotNull() {
        assertThat(exhibitionRepository).isNotNull();
    }

    @Test
    public void save_returns_savedExhibition() {
        // given
        final Exhibition exhibition = Exhibition.builder()
                .title("title")
                .description("description")
                .thumbnail("thumbnail")
                .build();

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
}
