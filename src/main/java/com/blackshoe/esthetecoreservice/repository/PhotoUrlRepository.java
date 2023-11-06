package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.PhotoUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoUrlRepository extends JpaRepository<PhotoUrl, Long> {
}
