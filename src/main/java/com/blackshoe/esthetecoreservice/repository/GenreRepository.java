package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findById(Long id);

    Optional<Genre> findByGenreId(UUID genreId);

    List<Genre> findAllByGenreIdIn(List<UUID> genreIds);
}
