package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;

import static org.assertj.core.api.Assertions.assertThat;

@EntityListeners(AuditingEntityListener.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void assert_isNotNull() {
        assertThat(userRepository).isNotNull();
    }

    @Test
    public void findByUserId_returns_user() {
        // given
        final User user = User.builder()
                .nickname("nickname")
                .biography("biography")
                .build();

        final User savedUser = userRepository.save(user);

        // when
        final User foundUser = userRepository.findByUserId(savedUser.getUserId()).orElse(null);

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getNickname()).isEqualTo(user.getNickname());
        assertThat(foundUser.getBiography()).isEqualTo(user.getBiography());
    }
}
