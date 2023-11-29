package com.blackshoe.esthetecoreservice.repository;

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

    //Support하는 User들이 많은 순 + List<Genre> genres 중 하나라도 가지고 있는 Photographers
    @Query("SELECT s.photographer FROM Support s " +
            "LEFT JOIN s.photographer.userGenres AS userGenres " +
            "LEFT JOIN userGenres.genre AS genre " +
            "WHERE (:userId IS NULL OR s.supportId = :userId) " +
            "AND (:genres IS NULL OR genre.genreName IN :genres) " +
            "GROUP BY s.photographer " +
            "ORDER BY COUNT(s.photographer) DESC")
    Page<User> getPhotographersBySupportCountAndGenres(@Param("userId") UUID userId, @Param("genres") List<String> genres, Pageable pageable);

    //Support하는 User들이 최근에 Support한 순 + List<Genre> genres 중 하나라도 가지고 있는 Photographers
    @Query("SELECT s.photographer FROM Support s " +
            "LEFT JOIN s.photographer.userGenres AS userGenres " +
            "LEFT JOIN userGenres.genre AS genre " +
            "WHERE (:userId IS NULL OR s.supportId = :userId) " +
            "AND (:genres IS NULL OR genre.genreName IN :genres) " +
            "GROUP BY s.photographer " +
            "ORDER BY MAX(s.createdAt) DESC")
    Page<User> getPhotographersByRecentSupportAndGenres(@Param("userId") UUID userId, @Param("genres") List<String> genres, Pageable pageable);

    //Support하는 User들이 많은 순 Photographers
    @Query("SELECT s.photographer FROM Support s " +
            "WHERE (:userId IS NULL OR s.supportId = :userId) " +
            "GROUP BY s.photographer " +
            "ORDER BY COUNT(s.photographer) DESC")
    Page<User> getPhotographersBySupportCount(@Param("userId") UUID userId, Pageable pageable);

    //Support하는 User들이 최근에 Support한 순 Photographers
    @Query("SELECT s.photographer FROM Support s " +
            "WHERE (:userId IS NULL OR s.supportId = :userId) " +
            "GROUP BY s.photographer " +
            "ORDER BY MAX(s.createdAt) DESC")
    Page<User> getPhotographersByRecentSupport(@Param("userId") UUID userId, Pageable pageable);

    //최근 7일간 Support한 유저들이 많은 순 Photographers
    @Query("SELECT s.photographer FROM Support s " +
            "WHERE (:userId IS NULL OR s.supportId = :userId) " +
            "AND s.createdAt >= CURRENT_DATE - 7 " +
            "GROUP BY s.photographer " +
            "ORDER BY COUNT(s.photographer) DESC")
    Page<User> getPhotographersBySupportCountInAWeek(@Param("userId") UUID userId, Pageable pageable);

    //최근 7일간 Support한 유저들이 많은 순 + List<Genre> genres 중 하나라도 가지고 있는 Photographers
    @Query("SELECT s.photographer FROM Support s " +
            "LEFT JOIN s.photographer.userGenres AS userGenres " +
            "LEFT JOIN userGenres.genre AS genre " +
            "WHERE (:userId IS NULL OR s.supportId = :userId) " +
            "AND (:genres IS NULL OR genre.genreName IN :genres) " +
            "AND s.createdAt >= CURRENT_DATE - 7 " +
            "GROUP BY s.photographer " +
            "ORDER BY COUNT(s.photographer) DESC")
    Page<User> getPhotographersBySupportCountInAWeekAndGenres(@Param("userId") UUID userId,@Param("genres") List<String> genres, Pageable pageable);
}
