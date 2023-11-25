package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @EntityGraph(attributePaths = "photoGenres")
    Optional<Photo> findByPhotoId(UUID photoId);

    Page<PhotoDto.ReadResponse> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
