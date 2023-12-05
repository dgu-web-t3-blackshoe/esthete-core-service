package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.dto.UserDto;
import com.blackshoe.esthetecoreservice.entity.Genre;
import com.blackshoe.esthetecoreservice.entity.Support;
import com.blackshoe.esthetecoreservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {

    @Query("SELECT s " +
            "FROM Support s " +
            "WHERE s.user.userId = :userId AND s.photographer.userId = :photographerId")
    Optional<Support> findByUserIdAndPhotographerId(@Param("userId") UUID userId, @Param("photographerId") UUID photographerId);

    @Query("SELECT s " +
            "FROM Support s " +
            "WHERE s.photographer.userId = :photographerId")
    List<Support> findAllByPhotographerId(@Param("photographerId") UUID photographerId);

    @Query("SELECT s.photographer " +
            "FROM Support s " +
            "WHERE s.user.userId = :userId")
    List<User> findAllBySupporterId(@Param("userId") UUID userId);

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.UserDto$SearchResult(s.photographer) " +
            "FROM Support s " +
            "WHERE s.user.userId = :userId and s.photographer.nickname like %:nickname% ")
    Page<UserDto.SearchResult> findAllByNicknameContaining(@Param("userId") UUID userId, @Param("nickname") String nickname, Pageable pageable);

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.UserDto$SearchResult(s.photographer) " +
            "FROM Support s " +
            "JOIN s.photographer.userGenres ug " +
            "WHERE s.user.userId = :userId and ug.genre.genreId in :searchGenreIds ")
    Page<UserDto.SearchResult> findAllByGenreContaining(@Param("userId") UUID userId, @Param("searchGenreIds") List<UUID> searchGenreIds, Pageable pageable);

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.UserDto$SearchResult(s.photographer) " +
            "FROM Support s " +
            "JOIN s.photographer.userGenres ug " +
            "WHERE s.user.userId = :userId and s.photographer.nickname like %:nickname% and ug.genre.genreId in :searchGenreIds")
    Page<UserDto.SearchResult> findAllByNicknameAndGenresContaining(@Param("userId") UUID userId, @Param("nickname") String nickname, @Param("searchGenreIds") List<UUID> searchGenreIds, Pageable pageable);

    Boolean existsByUserAndPhotographer(User user, User photographer);
}
