package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.PhotoGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoGenreRepository extends JpaRepository<PhotoGenre, Long> {
}
