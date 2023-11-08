package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    Optional<Exhibition> findByExhibitionId(UUID exhibitionId);
}
