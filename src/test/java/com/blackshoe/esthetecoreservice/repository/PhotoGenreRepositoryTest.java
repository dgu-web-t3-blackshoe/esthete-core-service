package com.blackshoe.esthetecoreservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PhotoGenreRepositoryTest {
    @Autowired
    private PhotoGenreRepository photoGenreRepository;

    @Test
    public void assert_isNotNull() {
        assertThat(photoGenreRepository).isNotNull();
    }
}
