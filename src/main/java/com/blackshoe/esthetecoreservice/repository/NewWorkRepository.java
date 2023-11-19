package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.NewWork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NewWorkRepository extends JpaRepository<NewWork, Long> {

    NewWork findByPhotographerId(UUID photographerId);

    NewWork findByPhotographerIdAndPhotoId(UUID photographerId, UUID photoId);
    NewWork findByPhotographerIdAndExhibitionId(UUID photographerId, UUID exhibitionId);
}
