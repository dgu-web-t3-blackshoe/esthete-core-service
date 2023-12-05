package com.blackshoe.esthetecoreservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PhotoChecksumService {
    void addPhotoChecksum(MultipartFile file, UUID photoId);

    void validatePhotoChecksumExist(MultipartFile file);

    void testAddPhotoChecksum(MultipartFile file, UUID photoId);

    void testValidatePhotoChecksumExist(MultipartFile file);
}
