package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface ViewRepository extends JpaRepository<View, UUID> {
    @Query("SELECT v FROM View v WHERE v.photo = ?1 AND v.user = ?2")
    Optional<View> findByPhotoAndUser(Photo photo, User user);

}
