package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.GuestBook;
import com.blackshoe.esthetecoreservice.entity.ProfileImgUrl;
import com.blackshoe.esthetecoreservice.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class GuestBookRepositoryTest {

    @Autowired
    private GuestBookRepository guestBookRepository;

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
        assertThat(guestBookRepository).isNotNull();
    }

    @Test
    public void save_returns_savedGuestBook() {
        // given
        photographer.setProfileImgUrl(profileImgUrl1);
        User savedPhotographer = userRepository.save(photographer);

        user.setProfileImgUrl(profileImgUrl2);
        User savedUser = userRepository.save(user);

        GuestBook guestBook = GuestBook.builder()
                .user(savedUser)
                .content("content")
                .build();

        guestBook.setPhotographer(savedPhotographer);

        // when
        final GuestBook savedGuestBook = guestBookRepository.save(guestBook);

        // then
        assertThat(savedGuestBook).isNotNull();
        assertThat(savedGuestBook.getId()).isNotNull();
        assertThat(savedGuestBook.getCreatedAt()).isNotNull();
        assertThat(savedGuestBook.getUser()).isEqualTo(guestBook.getUser());
        assertThat(savedGuestBook.getPhotographer()).isEqualTo(guestBook.getPhotographer());
        assertThat(savedGuestBook.getContent()).isEqualTo(guestBook.getContent());
    }
}
