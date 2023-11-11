package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.PhotoView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhotoViewRepository extends JpaRepository<PhotoView, Long> {

    @Query(value = "SELECT COUNT(*) FROM photo_views WHERE photo_uuid = ?1", nativeQuery = true)
    long countByPhotoId(UUID photoId);
}
