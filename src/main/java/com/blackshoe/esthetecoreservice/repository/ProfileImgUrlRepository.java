package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.ProfileImgUrl;
import com.blackshoe.esthetecoreservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImgUrlRepository extends JpaRepository<ProfileImgUrl, Long> {

    void deleteByS3Url(String s3Url);
}
