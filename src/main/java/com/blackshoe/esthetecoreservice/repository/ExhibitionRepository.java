package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.Exhibition;
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

    default Optional<Exhibition> findMostRecentExhibitionOfUser(@Param("userId") UUID userId) {
        final Page<Exhibition> exhibitionPage
                = findMostRecentExhibitionOfUser(userId, PageRequest.of(0, 1));

        return Optional.ofNullable(exhibitionPage.getContent().size() > 0 ?
                exhibitionPage.getContent().get(0) : null);
    };

    @Query(value = "SELECT e " +
            "FROM Exhibition e " +
            "WHERE e.user.userId = :userId " +
            "ORDER BY e.createdAt DESC ")
    Page<Exhibition> findMostRecentExhibitionOfUser(@Param("userId") UUID userId, Pageable pageable);

}
