package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.NewWork;
import com.blackshoe.esthetecoreservice.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NewWorkRepository extends JpaRepository<NewWork, Long> {

    Optional<NewWork> findByPhotographerIdAndExhibitionId(UUID photographerId, UUID exhibitionId);

    void deleteByExhibition(Exhibition exhibition);
}
