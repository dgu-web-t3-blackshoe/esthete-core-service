package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.dto.RoomPhotoDto;
import com.blackshoe.esthetecoreservice.entity.RoomPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomPhotoRepository extends JpaRepository<RoomPhoto, Long> {
    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.RoomPhotoDto(rp) " +
            "FROM RoomPhoto rp " +
            "WHERE rp.room.roomId = :roomId")
    List<RoomPhotoDto> findAllByRoomId(@Param("roomId") UUID roomId);
}
