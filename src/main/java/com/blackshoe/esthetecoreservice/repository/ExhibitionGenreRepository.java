package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.ExhibitionGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExhibitionGenreRepository extends JpaRepository<ExhibitionGenre, Long> {
    @Query("SELECT eg FROM ExhibitionGenre eg JOIN FETCH eg.exhibition e JOIN FETCH eg.genre g")
    List<ExhibitionGenre> findAllWithExhibitionAndGenre();
}
