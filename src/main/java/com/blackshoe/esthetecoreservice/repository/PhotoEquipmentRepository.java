package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.PhotoEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotoEquipmentRepository extends JpaRepository<PhotoEquipment, Long> {

    Optional<List<PhotoEquipment>> findByPhoto(Photo photo);
}
