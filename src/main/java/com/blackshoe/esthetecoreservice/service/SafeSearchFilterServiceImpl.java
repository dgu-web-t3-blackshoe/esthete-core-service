package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.exception.PhotoErrorResult;
import com.blackshoe.esthetecoreservice.exception.PhotoException;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SafeSearchFilterServiceImpl implements SafeSearchFilterService{

    private final CloudVisionTemplate cloudVisionTemplate;

    public void safeSearchFilter(MultipartFile file) {

        AnnotateImageResponse response = cloudVisionTemplate.analyzeImage(
                file.getResource(), Feature.Type.SAFE_SEARCH_DETECTION);

        Map<String, Float> imageLabels = response.getLabelAnnotationsList().stream()
                .collect(
                        Collectors.toMap(
                                EntityAnnotation::getDescription,
                                EntityAnnotation::getScore,
                                (u, v) -> {
                                    throw new PhotoException(PhotoErrorResult.PHOTO_SAFE_SEARCH_FAILED);
                                },
                                java.util.LinkedHashMap::new
                        )
                );

        if(imageLabels.get("adult") > 0.5 || imageLabels.get("racy") > 0.5 || imageLabels.get("spoof") > 0.5 || imageLabels.get("medical") > 0.5 || imageLabels.get("violence") > 0.5) {
            throw new PhotoException(PhotoErrorResult.UNSAFE_PHOTO);
        }
    }
}
