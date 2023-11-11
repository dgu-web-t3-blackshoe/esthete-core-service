package com.blackshoe.esthetecoreservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class PhotoViewRepositoryTest {

    @Autowired
    private PhotoViewRepository photoViewRepository;

    @Test
    public void assert_isNotNull() {
        assertThat(photoViewRepository).isNotNull();
    }
}
