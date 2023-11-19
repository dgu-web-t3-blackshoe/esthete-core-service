package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.dto.UserDto;
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
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(UUID any);

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.UserDto$SearchResult(u) " +
            "FROM User u " +
            "WHERE u.nickname like %:nickname% ")
    Page<UserDto.SearchResult> findAllByNicknameContaining(@Param("nickname") String nickname, Pageable pageable);

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.UserDto$SearchResult(u) " +
            "FROM User u " +
            "JOIN u.userGenres ug " +
            "WHERE ug.genre.genreId in :searchGenreIds ")
    Page<UserDto.SearchResult> findAllByGenresContaining(@Param("searchGenreIds") List<UUID> searchGenreIds, Pageable pageable);

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.UserDto$SearchResult(u) " +
            "FROM User u " +
            "JOIN u.userGenres ug " +
            "WHERE u.nickname like %:nickname% and ug.genre.genreId in :searchGenreIds")
    Page<UserDto.SearchResult> findAllByNicknameAndGenresContaining(@Param("nickname") String nickname, @Param("searchGenreIds") List<UUID> searchGenreIds, Pageable pageable);
}
