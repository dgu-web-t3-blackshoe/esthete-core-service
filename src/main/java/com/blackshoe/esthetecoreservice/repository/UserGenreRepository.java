package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.UserGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGenreRepository extends JpaRepository<UserGenre, Long> {
}
