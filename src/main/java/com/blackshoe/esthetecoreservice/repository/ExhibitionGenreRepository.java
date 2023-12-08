package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.ExhibitionGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ExhibitionGenreRepository extends JpaRepository<ExhibitionGenre, Long> {

    Optional<ExhibitionGenre> findByExhibitionIdAndGenreId(Long exhibitionId, Long genreId);
}
