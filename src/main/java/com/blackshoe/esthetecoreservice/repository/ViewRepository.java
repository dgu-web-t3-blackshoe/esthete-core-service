package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.entity.View;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ViewRepository extends JpaRepository<View, UUID> {
    //get photos by user id order by view count
    @Query("SELECT v.photo FROM View v WHERE v.user = :user GROUP BY v.photo ORDER BY COUNT(v.photo) DESC")
    Page<Photo> findPhotosByUserIdOrderByViewCount(User user, Pageable pageable);

    @Query("SELECT v FROM View v JOIN FETCH v.user u JOIN FETCH v.exhibition e")
    List<View> findAllWithUserAndExhibition();

}
