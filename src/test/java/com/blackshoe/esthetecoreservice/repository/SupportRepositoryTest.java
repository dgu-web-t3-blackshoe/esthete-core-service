package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.ProfileImgUrl;
import com.blackshoe.esthetecoreservice.entity.Support;
import com.blackshoe.esthetecoreservice.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@EntityListeners(AuditingEntityListener.class)
@DataJpaTest
public class SupportRepositoryTest {

    @Autowired
    private SupportRepository supportRepository;

    @Autowired
    private UserRepository userRepository;

    private final User photographer = User.builder()
            .nickname("nickname")
            .biography("biography")
            .build();

    private final ProfileImgUrl profileImgUrl1 = ProfileImgUrl.builder()
            .cloudfrontUrl("cloudfrontUrl")
            .s3Url("s3Url")
            .build();

    private final User user = User.builder()
            .nickname("nickname")
            .biography("biography")
            .build();

    private final ProfileImgUrl profileImgUrl2 = ProfileImgUrl.builder()
            .cloudfrontUrl("cloudfrontUrl")
            .s3Url("s3Url")
            .build();

    @Test
    public void assert_isNotNull() {
        assertThat(supportRepository).isNotNull();
    }

    @Test
    public void save_returns_savedSupport() {
        // given
        photographer.setProfileImgUrl(profileImgUrl1);
        User savedPhotographer = userRepository.save(photographer);

        user.setProfileImgUrl(profileImgUrl2);
        User savedUser = userRepository.save(user);

        final Support support = Support.builder()
                .photographer(savedPhotographer)
                .build();

        support.setUser(savedUser);

        // when
        Support savedSupport = supportRepository.save(support);

        // then
        assertThat(savedSupport).isNotNull();
        assertThat(savedSupport.getUser()).isEqualTo(savedUser);
        assertThat(savedSupport.getPhotographer()).isEqualTo(savedPhotographer);
    }

    @Test
    public void save_uniqueSupport_onlyOnce() {
        // given
        photographer.setProfileImgUrl(profileImgUrl1);
        User savedPhotographer = userRepository.save(photographer);

        user.setProfileImgUrl(profileImgUrl2);
        User savedUser = userRepository.save(user);

        final Support support1 = Support.builder()
                .photographer(savedPhotographer)
                .build();

        support1.setUser(savedUser);

        final Support support2 = Support.builder()
                .photographer(savedPhotographer)
                .build();

        support2.setUser(savedUser);

        // when
        Support savedSupport = supportRepository.save(support1);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> supportRepository.save(support2));
    }

    @Test
    public void findByUserIdAndPhotographerId_returns_optionalSupport() {
        // given
        photographer.setProfileImgUrl(profileImgUrl1);
        User savedPhotographer = userRepository.save(photographer);

        user.setProfileImgUrl(profileImgUrl2);
        User savedUser = userRepository.save(user);

        final Support support = Support.builder()
                .photographer(savedPhotographer)
                .build();

        support.setUser(savedUser);

        Support savedSupport = supportRepository.save(support);

        // when
        Support foundSupport = supportRepository.findByUserIdAndPhotographerId(savedUser.getUserId(), savedPhotographer.getUserId()).get();

        // then
        assertThat(foundSupport).isNotNull();
        assertThat(foundSupport).isEqualTo(savedSupport);
    }
}
