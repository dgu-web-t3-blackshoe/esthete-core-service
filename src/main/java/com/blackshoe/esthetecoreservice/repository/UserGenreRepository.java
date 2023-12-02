package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.UserGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGenreRepository extends JpaRepository<UserGenre, Long> {
    @Query("SELECT ug FROM UserGenre ug JOIN FETCH ug.user u JOIN FETCH ug.genre g")
    List<UserGenre> findAllWithUserAndGenre();
}
