package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.dto.PhotoDto;
import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.vo.PhotoAddressFilter;
import com.blackshoe.esthetecoreservice.vo.PhotoPointFilter;
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


    default Page<PhotoDto.ReadRegionGroupResponse> findTop10ByUserLocationGroupByState(PhotoPointFilter photoPointFilter) {
        return findByUserLocationGroupByState(photoPointFilter, PageRequest.of(0, 10));
    }

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.PhotoDto$ReadRegionGroupResponse(" +
            "p.photoLocation.state, " +
            "p.photoLocation.city, " +
            "p.photoLocation.town, " +
            "p.photoUrl.cloudfrontUrl, " +
            "count(p)) " +
            "FROM Photo p " +
            "WHERE p.photoLocation.latitude " +
            "BETWEEN :#{#photoPointFilter.latitude - #photoPointFilter.latitudeDelta} " +
            "AND :#{#photoPointFilter.latitude + #photoPointFilter.latitudeDelta} " +
            "AND p.photoLocation.longitude " +
            "BETWEEN :#{#photoPointFilter.longitude - #photoPointFilter.longitudeDelta} " +
            "AND :#{#photoPointFilter.longitude + #photoPointFilter.longitudeDelta} " +
            "GROUP BY p.photoLocation.state " +
            "ORDER BY count(p) DESC")
    Page<PhotoDto.ReadRegionGroupResponse> findByUserLocationGroupByState(
            @Param("photoPointFilter") PhotoPointFilter photoPointFilter, Pageable pageable);

    default Page<PhotoDto.ReadRegionGroupResponse> findTop10ByUserLocationGroupByCity(PhotoPointFilter photoPointFilter) {
        return findByUserLocationGroupByCity(photoPointFilter, PageRequest.of(0, 10));
    }

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.PhotoDto$ReadRegionGroupResponse(" +
            "p.photoLocation.state, " +
            "p.photoLocation.city, " +
            "p.photoLocation.town, " +
            "p.photoUrl.cloudfrontUrl, " +
            "count(p)) " +
            "FROM Photo p " +
            "WHERE p.photoLocation.latitude " +
            "BETWEEN :#{#photoPointFilter.latitude - #photoPointFilter.latitudeDelta} " +
            "AND :#{#photoPointFilter.latitude + #photoPointFilter.latitudeDelta} " +
            "AND p.photoLocation.longitude " +
            "BETWEEN :#{#photoPointFilter.longitude - #photoPointFilter.longitudeDelta} " +
            "AND :#{#photoPointFilter.longitude + #photoPointFilter.longitudeDelta} " +
            "GROUP BY p.photoLocation.city " +
            "ORDER BY count(p) DESC")
    Page<PhotoDto.ReadRegionGroupResponse> findByUserLocationGroupByCity(
            @Param("photoPointFilter") PhotoPointFilter photoPointFilter, Pageable pageable);

    default Page<PhotoDto.ReadRegionGroupResponse> findTop10ByUserLocationGroupByTown(PhotoPointFilter photoPointFilter) {
        return findByUserLocationGroupByTown(photoPointFilter, PageRequest.of(0, 10));
    }

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.PhotoDto$ReadRegionGroupResponse(" +
            "p.photoLocation.state, " +
            "p.photoLocation.city, " +
            "p.photoLocation.town, " +
            "p.photoUrl.cloudfrontUrl, " +
            "count(p)) " +
            "FROM Photo p " +
            "WHERE p.photoLocation.latitude " +
            "BETWEEN :#{#photoPointFilter.latitude - #photoPointFilter.latitudeDelta} " +
            "AND :#{#photoPointFilter.latitude + #photoPointFilter.latitudeDelta} " +
            "AND p.photoLocation.longitude " +
            "BETWEEN :#{#photoPointFilter.longitude - #photoPointFilter.longitudeDelta} " +
            "AND :#{#photoPointFilter.longitude + #photoPointFilter.longitudeDelta} " +
            "GROUP BY p.photoLocation.town " +
            "ORDER BY count(p) DESC")
    Page<PhotoDto.ReadRegionGroupResponse> findByUserLocationGroupByTown(
            @Param("photoPointFilter") PhotoPointFilter photoPointFilter, Pageable pageable);

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.PhotoDto$ReadResponse(p) " +
            "FROM Photo p " +
            "WHERE p.photoLocation.state = :#{#photoAddressFilter.state} ")
    Page<PhotoDto.ReadResponse> findAllByPhotoLocationState(
            @Param("photoAddressFilter") PhotoAddressFilter photoAddressFilter, Pageable pageable);

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.PhotoDto$ReadResponse(p) " +
            "FROM Photo p " +
            "WHERE p.photoLocation.state = :#{#photoAddressFilter.state} " +
            "AND p.photoLocation.city = :#{#photoAddressFilter.city} ")
    Page<PhotoDto.ReadResponse> findAllByPhotoLocationStateAndCity(
            @Param("photoAddressFilter") PhotoAddressFilter photoAddressFilter, Pageable pageable);

    @Query("SELECT new com.blackshoe.esthetecoreservice.dto.PhotoDto$ReadResponse(p) " +
            "FROM Photo p " +
            "WHERE p.photoLocation.state = :#{#photoAddressFilter.state} " +
            "AND p.photoLocation.city = :#{#photoAddressFilter.city} " +
            "AND p.photoLocation.town = :#{#photoAddressFilter.town} ")
    Page<PhotoDto.ReadResponse> findAllByPhotoLocationStateAndCityAndTown(
            @Param("photoAddressFilter") PhotoAddressFilter photoAddressFilter, Pageable pageable);
}
