package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.ProfileImgUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImgUrlRepository extends JpaRepository<ProfileImgUrl, Long> {
}
