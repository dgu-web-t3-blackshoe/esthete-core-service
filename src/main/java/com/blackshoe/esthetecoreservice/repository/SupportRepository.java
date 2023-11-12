package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {
    @Query("SELECT s " +
            "FROM Support s " +
            "WHERE s.user.userId = :userId AND s.photographer.userId = :photographerId")
    Optional<Support> findByUserIdAndPhotographerId(@Param("userId") UUID userId, @Param("photographerId") UUID photographerId);
}
