package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.PhotoChecksum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoChecksumRepository extends JpaRepository<PhotoChecksum, Long> {
    boolean existsByChecksum(String checksum);

    void deleteByChecksum(String checksum);

    Optional<PhotoChecksum> findByChecksum(String checksum);

    void deleteAllByChecksum(String checksum);
}
