package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.dto.RoomDto;
import com.blackshoe.esthetecoreservice.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomId(UUID roomId);

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.RoomDto(r.roomId, r.title, r.description, r.thumbnail) " +
            "FROM Room r " +
            "WHERE r.exhibition.exhibitionId = :exhibitionId " +
            "ORDER BY r.id ASC " )
    List<RoomDto> findAllByExhibitionId(@Param("exhibitionId") UUID exhibitionId);
}