package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.RoomPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomPhotoRepository extends JpaRepository<RoomPhoto, Long> {
}
