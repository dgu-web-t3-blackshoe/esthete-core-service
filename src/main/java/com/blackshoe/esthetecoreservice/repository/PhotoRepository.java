package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Photo findByPhotoId(UUID photoId);
}
