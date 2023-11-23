package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    Optional<Exhibition> findByExhibitionId(UUID exhibitionId);

    Optional<Exhibition> findTopByUserIdOrderByCreatedAtDesc(UUID userId);

    default Optional<Exhibition> findMostRecentExhibitionOfUser(@Param("userId") UUID userId) {
        return findTopByUserIdOrderByCreatedAtDesc(userId);
    };

    Page<ExhibitionDto.ReadResponse> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
