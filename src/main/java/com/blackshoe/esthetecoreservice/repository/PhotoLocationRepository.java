package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.PhotoLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PhotoLocationRepository extends JpaRepository<PhotoLocation, Long> {
}
