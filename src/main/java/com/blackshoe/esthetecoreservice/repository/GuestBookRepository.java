package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.dto.GuestBookDto;
import com.blackshoe.esthetecoreservice.entity.GuestBook;
import com.blackshoe.esthetecoreservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.GuestBookDto$ReadGuestBookResponse(g) FROM GuestBook g WHERE g.photographer.userId = :userId ORDER BY g.createdAt DESC")
    Page<GuestBookDto.ReadGuestBookResponse> findByUserOrderByCreatedAtDesc(@Param("userId") UUID userId, Pageable pageable);
}
