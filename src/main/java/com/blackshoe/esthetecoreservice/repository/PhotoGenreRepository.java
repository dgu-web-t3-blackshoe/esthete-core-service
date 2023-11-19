package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.PhotoGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotoGenreRepository extends JpaRepository<PhotoGenre, Long> {
    Optional<List<PhotoGenre>> findByPhoto(Photo photo);
}
