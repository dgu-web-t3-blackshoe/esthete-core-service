package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.NewWork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NewWorkRepository extends JpaRepository<NewWork, Long> {

    Optional<NewWork> findByPhotographerId(UUID photographerId);

    Optional<NewWork> findByPhotographerIdAndPhotoId(UUID photographerId, UUID photoId);
    Optional<NewWork> findByPhotographerIdAndExhibitionId(UUID photographerId, UUID exhibitionId);
}
