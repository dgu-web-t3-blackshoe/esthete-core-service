package com.blackshoe.esthetecoreservice.service;

import lombok.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

public interface SafeSearchFilterService {
    void safeSearchFilter(MultipartFile file);
}
