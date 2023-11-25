package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.vo.PhotoLocationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @EntityGraph(attributePaths = "photoGenres")
    Optional<Photo> findByPhotoId(UUID photoId);

    Page<PhotoDto.ReadResponse> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);


    default Page<PhotoDto.ReadRegionGroupResponse> findTop10ByUserLocationGroupByState(PhotoLocationFilter photoLocationFilter) {
        return findByUserLocationGroupByState(photoLocationFilter, PageRequest.of(0, 10));
    }

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.PhotoDto$ReadRegionGroupResponse(" +
            "p.photoLocation.state, " +
            "p.photoLocation.city, " +
            "p.photoLocation.town, " +
            "p.photoUrl.cloudfrontUrl, " +
            "count(p)) " +
            "FROM Photo p " +
            "WHERE p.photoLocation.latitude " +
            "BETWEEN :#{#photoLocationFilter.latitude - #photoLocationFilter.latitudeDelta} " +
            "AND :#{#photoLocationFilter.latitude + #photoLocationFilter.latitudeDelta} " +
            "AND p.photoLocation.longitude " +
            "BETWEEN :#{#photoLocationFilter.longitude - #photoLocationFilter.longitudeDelta} " +
            "AND :#{#photoLocationFilter.longitude + #photoLocationFilter.longitudeDelta} " +
            "GROUP BY p.photoLocation.state " +
            "ORDER BY count(p) DESC")
    Page<PhotoDto.ReadRegionGroupResponse> findByUserLocationGroupByState(
            @Param("photoLocationFilter") PhotoLocationFilter photoLocationFilter, Pageable pageable);

    default Page<PhotoDto.ReadRegionGroupResponse> findTop10ByUserLocationGroupByCity(PhotoLocationFilter photoLocationFilter) {
        return findByUserLocationGroupByCity(photoLocationFilter, PageRequest.of(0, 10));
    }

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.PhotoDto$ReadRegionGroupResponse(" +
            "p.photoLocation.state, " +
            "p.photoLocation.city, " +
            "p.photoLocation.town, " +
            "p.photoUrl.cloudfrontUrl, " +
            "count(p)) " +
            "FROM Photo p " +
            "WHERE p.photoLocation.latitude " +
            "BETWEEN :#{#photoLocationFilter.latitude - #photoLocationFilter.latitudeDelta} " +
            "AND :#{#photoLocationFilter.latitude + #photoLocationFilter.latitudeDelta} " +
            "AND p.photoLocation.longitude " +
            "BETWEEN :#{#photoLocationFilter.longitude - #photoLocationFilter.longitudeDelta} " +
            "AND :#{#photoLocationFilter.longitude + #photoLocationFilter.longitudeDelta} " +
            "GROUP BY p.photoLocation.city " +
            "ORDER BY count(p) DESC")
    Page<PhotoDto.ReadRegionGroupResponse> findByUserLocationGroupByCity(
            @Param("photoLocationFilter") PhotoLocationFilter photoLocationFilter, Pageable pageable);

    default Page<PhotoDto.ReadRegionGroupResponse> findTop10ByUserLocationGroupByTown(PhotoLocationFilter photoLocationFilter) {
        return findByUserLocationGroupByTown(photoLocationFilter, PageRequest.of(0, 10));
    }

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.PhotoDto$ReadRegionGroupResponse(" +
            "p.photoLocation.state, " +
            "p.photoLocation.city, " +
            "p.photoLocation.town, " +
            "p.photoUrl.cloudfrontUrl, " +
            "count(p)) " +
            "FROM Photo p " +
            "WHERE p.photoLocation.latitude " +
            "BETWEEN :#{#photoLocationFilter.latitude - #photoLocationFilter.latitudeDelta} " +
            "AND :#{#photoLocationFilter.latitude + #photoLocationFilter.latitudeDelta} " +
            "AND p.photoLocation.longitude " +
            "BETWEEN :#{#photoLocationFilter.longitude - #photoLocationFilter.longitudeDelta} " +
            "AND :#{#photoLocationFilter.longitude + #photoLocationFilter.longitudeDelta} " +
            "GROUP BY p.photoLocation.town " +
            "ORDER BY count(p) DESC")
    Page<PhotoDto.ReadRegionGroupResponse> findByUserLocationGroupByTown(
            @Param("photoLocationFilter") PhotoLocationFilter photoLocationFilter, Pageable pageable);
}
