package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.SafeSearchErrorDto;
import com.blackshoe.esthetecoreservice.exception.PhotoErrorResult;
import com.blackshoe.esthetecoreservice.exception.PhotoException;
import com.blackshoe.esthetecoreservice.exception.SafeSearchException;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class SafeSearchFilterServiceImpl implements SafeSearchFilterService {

    private final CloudVisionTemplate cloudVisionTemplate;

    @Value("${cloud.gcp.vision.safe-search-option}")
    private int SAFE_SEARCH_OPTION;

    public void safeSearchFilter(MultipartFile file) {

        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            log.error("Error reading file", e);
            throw new PhotoException(PhotoErrorResult.PHOTO_SAFE_SEARCH_FAILED);
        }

        AnnotateImageResponse response = cloudVisionTemplate.analyzeImage(
                new InputStreamResource(inputStream), Feature.Type.SAFE_SEARCH_DETECTION);

        SafeSearchAnnotation safeSearchAnnotation = response.getSafeSearchAnnotation();

        log.info("SafeSearchFilterServiceImpl safeSearchAnnotation : {}", safeSearchAnnotation.toString());

        if (safeSearchAnnotation.getAdultValue() > SAFE_SEARCH_OPTION || safeSearchAnnotation.getRacyValue() > SAFE_SEARCH_OPTION ||
                safeSearchAnnotation.getSpoofValue() > SAFE_SEARCH_OPTION || safeSearchAnnotation.getMedicalValue() > SAFE_SEARCH_OPTION ||
                safeSearchAnnotation.getViolenceValue() > SAFE_SEARCH_OPTION) {
            log.info("Unsafe photo detected adult : {}", safeSearchAnnotation.getAdult());
            log.info("Unsafe photo detected racy : {}", safeSearchAnnotation.getRacy());
            log.info("Unsafe photo detected spoof : {}", safeSearchAnnotation.getSpoof());
            log.info("Unsafe photo detected medical : {}", safeSearchAnnotation.getMedical());
            log.info("Unsafe photo detected violence : {}", safeSearchAnnotation.getViolence());

            SafeSearchErrorDto.SafeSearchData safeSearchData = SafeSearchErrorDto.SafeSearchData.builder()
                    .adult(safeSearchAnnotation.getAdult().toString())
                    .racy(safeSearchAnnotation.getRacy().toString())
                    .spoof(safeSearchAnnotation.getSpoof().toString())
                    .medical(safeSearchAnnotation.getMedical().toString())
                    .violence(safeSearchAnnotation.getViolence().toString())
                    .build();

            throw new SafeSearchException(
                    "선정적인 사진은 업로드할 수 없습니다",
                    safeSearchData);
        }
    }
}

