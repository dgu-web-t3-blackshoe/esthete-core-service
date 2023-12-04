package com.blackshoe.esthetecoreservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface SafeSearchFilterService {
    void safeSearchFilter(MultipartFile file);
}
