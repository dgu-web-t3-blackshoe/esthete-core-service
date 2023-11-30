package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.dto.GuestBookDto;
import com.blackshoe.esthetecoreservice.entity.GuestBook;
import com.blackshoe.esthetecoreservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {

    Page<GuestBookDto.ReadResponse> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Optional<GuestBook> findByGuestBookId(UUID guestBookId);
}
