package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.dto.UserDto;
import com.blackshoe.esthetecoreservice.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@EntityListeners(AuditingEntityListener.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileImgUrlRepository profileImgUrlRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private UserGenreRepository userGenreRepository;

    @Test
    public void assert_isNotNull() {
        assertThat(userRepository).isNotNull();
    }

    final User user = User.builder()
            .nickname("nickname")
            .biography("biography")
            .build();

    final ProfileImgUrl profileImgUrl = ProfileImgUrl.builder()
            .cloudfrontUrl("cloudfrontUrl")
            .s3Url("s3Url")
            .build();

    final Genre genre = Genre.builder()
            .genreName("genreName")
            .build();

    final Photo photo = Photo.builder()
            .title("title")
            .description("description")
            .time("time")
            .build();

    final PhotoUrl photoUrl = PhotoUrl.builder()
            .cloudfrontUrl("cloudfrontUrl")
            .s3Url("s3Url")
            .build();

    @Test
    public void findByUserId_returns_user() {
        // given
        final User savedUser = userRepository.save(user);

        // when
        final User foundUser = userRepository.findByUserId(savedUser.getUserId()).orElse(null);

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getNickname()).isEqualTo(user.getNickname());
        assertThat(foundUser.getBiography()).isEqualTo(user.getBiography());
    }

    @Test
    public void findAllByNicknameContaining_returns_userList() {
        // given
        user.setProfileImgUrl(profileImgUrl);
        final User savedUser = userRepository.save(user);

        photo.setPhotoUrl(photoUrl);
        photo.setUser(savedUser);
        final Photo savedPhoto = photoRepository.save(photo);

        final Genre savedGenre = genreRepository.save(genre);

        final UserGenre userGenre = UserGenre.builder()
                .genre(savedGenre)
                .build();
        userGenre.setUser(savedUser);
        userGenreRepository.save(userGenre);

        final Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        final Pageable Pageable = PageRequest.of(0, 10, sort);

        // when
        final Page<UserDto.SearchResult> foundUserPage = userRepository.findAllByNicknameContaining("nickname", Pageable);

        // then
        assertThat(foundUserPage).isNotNull();
        assertThat(foundUserPage.getContent().get(0).getPhotographerId()).isNotNull();
        assertThat(foundUserPage.getContent().get(0).getProfileImg()).isEqualTo(savedUser.getProfileImgUrl().getCloudfrontUrl());
        assertThat(foundUserPage.getContent().get(0).getNickname()).isEqualTo(savedUser.getNickname());
        assertThat(foundUserPage.getContent().get(0).getBiography()).isEqualTo(savedUser.getBiography());
        assertThat(foundUserPage.getContent().get(0).getGenres().get(0).getGenreId()).isEqualTo(savedGenre.getGenreId().toString());
        assertThat(foundUserPage.getContent().get(0).getGenres().get(0).getGenre()).isEqualTo(savedGenre.getGenreName());
        assertThat(foundUserPage.getContent().get(0).getHighlights().get(0).getPhotoId()).isEqualTo(savedPhoto.getPhotoId().toString());
        assertThat(foundUserPage.getContent().get(0).getHighlights().get(0).getPhoto()).isEqualTo(savedPhoto.getPhotoUrl().getCloudfrontUrl());
    }

    @Test
    public void findAllByGenresContaining_returns_userList() {
        // given
        user.setProfileImgUrl(profileImgUrl);
        final User savedUser = userRepository.save(user);

        photo.setPhotoUrl(photoUrl);
        photo.setUser(savedUser);
        final Photo savedPhoto = photoRepository.save(photo);

        final Genre savedGenre = genreRepository.save(genre);

        final UserGenre userGenre = UserGenre.builder()
                .genre(savedGenre)
                .build();
        userGenre.setUser(savedUser);
        userGenreRepository.save(userGenre);

        final Sort sort = Sort.by(Sort.Direction.DESC, "supportCount");
        final Pageable pageable = PageRequest.of(0, 10, sort);
        final List<UUID> searchGenres = List.of(savedGenre.getGenreId());

        // when
        final Page<UserDto.SearchResult> foundUserPage = userRepository.findAllByGenresContaining(searchGenres, pageable);

        // then
        assertThat(foundUserPage).isNotNull();
        assertThat(foundUserPage.getContent().get(0).getPhotographerId()).isNotNull();
        assertThat(foundUserPage.getContent().get(0).getProfileImg()).isEqualTo(savedUser.getProfileImgUrl().getCloudfrontUrl());
        assertThat(foundUserPage.getContent().get(0).getNickname()).isEqualTo(savedUser.getNickname());
        assertThat(foundUserPage.getContent().get(0).getBiography()).isEqualTo(savedUser.getBiography());
        assertThat(foundUserPage.getContent().get(0).getGenres().get(0).getGenreId()).isEqualTo(savedGenre.getGenreId().toString());
        assertThat(foundUserPage.getContent().get(0).getGenres().get(0).getGenre()).isEqualTo(savedGenre.getGenreName());
        assertThat(foundUserPage.getContent().get(0).getHighlights().get(0).getPhotoId()).isEqualTo(savedPhoto.getPhotoId().toString());
        assertThat(foundUserPage.getContent().get(0).getHighlights().get(0).getPhoto()).isEqualTo(savedPhoto.getPhotoUrl().getCloudfrontUrl());
    }

    @Test
    public void findAllByNicknameAndGenresContaining_returns_userList() {
        // given
        user.setProfileImgUrl(profileImgUrl);
        final User savedUser = userRepository.save(user);

        photo.setPhotoUrl(photoUrl);
        photo.setUser(savedUser);
        final Photo savedPhoto = photoRepository.save(photo);

        final Genre savedGenre = genreRepository.save(genre);

        final UserGenre userGenre = UserGenre.builder()
                .genre(savedGenre)
                .build();
        userGenre.setUser(savedUser);
        userGenreRepository.save(userGenre);

        final Sort sort = Sort.by(Sort.Direction.DESC, "viewCount");
        final Pageable pageable = PageRequest.of(0, 10, sort);
        final List<UUID> searchGenres = List.of(savedGenre.getGenreId());

        // when
        final Page<UserDto.SearchResult> foundUserPage = userRepository.findAllByNicknameAndGenresContaining(savedUser.getNickname(), searchGenres, pageable);

        // then
        assertThat(foundUserPage).isNotNull();
        assertThat(foundUserPage.getContent().get(0).getPhotographerId()).isNotNull();
        assertThat(foundUserPage.getContent().get(0).getProfileImg()).isEqualTo(savedUser.getProfileImgUrl().getCloudfrontUrl());
        assertThat(foundUserPage.getContent().get(0).getNickname()).isEqualTo(savedUser.getNickname());
        assertThat(foundUserPage.getContent().get(0).getBiography()).isEqualTo(savedUser.getBiography());
        assertThat(foundUserPage.getContent().get(0).getGenres().get(0).getGenreId()).isEqualTo(savedGenre.getGenreId().toString());
        assertThat(foundUserPage.getContent().get(0).getGenres().get(0).getGenre()).isEqualTo(savedGenre.getGenreName());
        assertThat(foundUserPage.getContent().get(0).getHighlights().get(0).getPhotoId()).isEqualTo(savedPhoto.getPhotoId().toString());
        assertThat(foundUserPage.getContent().get(0).getHighlights().get(0).getPhoto()).isEqualTo(savedPhoto.getPhotoUrl().getCloudfrontUrl());
    }
}
