package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.entity.*;
import com.blackshoe.esthetecoreservice.exception.PhotoErrorResult;
import com.blackshoe.esthetecoreservice.exception.PhotoException;
import com.blackshoe.esthetecoreservice.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service @Slf4j
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final ViewRepository viewRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final UserGenreRepository userGenreRepository;
    private final ExhibitionGenreRepository exhibitionGenreRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PhotoRepository photoRepository;

    private Map<Long, Set<Long>> collectUserPreferredGenres() {
        Map<Long, Set<Long>> userPreferredGenres = new HashMap<>();
        List<UserGenre> userGenres = userGenreRepository.findAll();
        for (UserGenre userGenre : userGenres) {
            long userId = userGenre.getUser().getId();
            long genreId = userGenre.getGenre().getId();
            userPreferredGenres.computeIfAbsent(userId, k -> new HashSet<>()).add(genreId);
        }
        return userPreferredGenres;
    }

    private Map<Long, Set<Long>> collectExhibitionGenres() {
        Map<Long, Set<Long>> exhibitionGenres = new HashMap<>();
        List<ExhibitionGenre> exhibitionGenreList = exhibitionGenreRepository.findAll();
        for (ExhibitionGenre exhibitionGenre : exhibitionGenreList) {
            long exhibitionId = exhibitionGenre.getExhibition().getId();
            long genreId = exhibitionGenre.getGenre().getId();
            exhibitionGenres.computeIfAbsent(exhibitionId, k -> new HashSet<>()).add(genreId);
        }
        return exhibitionGenres;
    }

    private float calculatePreferenceScore(long userId, long exhibitionId, Map<Long, Set<Long>> userPreferredGenres, Map<Long, Set<Long>> exhibitionGenres) {
        Set<Long> originalUserGenres = userPreferredGenres.getOrDefault(userId, new HashSet<>());
        Set<Long> exhibitionGenreIds = exhibitionGenres.getOrDefault(exhibitionId, new HashSet<>());

        // 원본 Set을 복사하여 작업
        Set<Long> userGenres = new HashSet<>(originalUserGenres);

        log.info("userGenres: {}", userGenres);
        log.info("exhibitionGenreIds: {}", exhibitionGenreIds);

        userGenres.retainAll(exhibitionGenreIds);
        //userGenres가 originalUserGenres의 몇 %인지
        float percentage = (float) userGenres.size() / (float) originalUserGenres.size() * 10;
        return percentage;
    }
    private void createOrUpdateCsvFile() throws IOException {
        String filePath = "user_file_data_model.csv"; // CSV 파일 경로 지정
        File file = new File(filePath);

        //파일 삭제
        if (file.exists()) {
            file.delete();
        }

        file.createNewFile();

        // FileWriter를 사용하여 파일에 데이터를 추가
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            List<View> views = viewRepository.findAll();
            for (View view : views) {
                long userId = view.getUser().getId();
                long exhibitionId = view.getExhibition().getId();
                float preferenceValue = calculatePreferenceScore(userId, exhibitionId, collectUserPreferredGenres(), collectExhibitionGenres());
                bw.write(userId + "," + exhibitionId + "," + preferenceValue + "\n");
            }
        }
    }

    private DataModel createFileDataModel() throws Exception {
        createOrUpdateCsvFile(); // CSV 파일 생성 또는 업데이트
        String fileLocation = "user_file_data_model.csv";

        //String fileLocation = "test.csv";
        return new FileDataModel(new File(fileLocation));
    }

    public List<RecommendedItem> recommendExhibitions(UUID userId, int numberOfRecommendations) throws Exception {
        long userLongId = getUserIdFromUUID(userId);
        //long userLongId = 2L;
        DataModel model = createFileDataModel();
        log.info("model: {}", model);

        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        log.info("similarity: {}", similarity.toString());

        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.0001, similarity, model);
        log.info("neighborhood: {}", neighborhood.toString());

        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        log.info("recommender: {}", recommender.toString());

        return recommender.recommend(userLongId, numberOfRecommendations);
    }

    private long getUserIdFromUUID(UUID userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new IllegalStateException("User not found")).getId();
    }

    @Override
    public List<ExhibitionDto.ReadRecommendedExhibitionResponse> getRecommendedExhibitions(UUID userId) throws Exception {
        List<RecommendedItem> recommendedItems = recommendExhibitions(userId, 1);
        log.info("recommendedItems: {}", recommendedItems);

        List<ExhibitionDto.ReadRecommendedExhibitionResponse> recommendedExhibitions = new ArrayList<>();

        for (RecommendedItem item : recommendedItems) {
            long exhibitionId = item.getItemID();
            Exhibition exhibition = exhibitionRepository.findById(exhibitionId)
                    .orElseThrow(() -> new IllegalStateException("Exhibition not found"));

            Photo photo = photoRepository.findByPhotoId(UUID.fromString(exhibition.getThumbnail()))
                    .orElseThrow(() -> new PhotoException(PhotoErrorResult.PHOTO_NOT_FOUND));

            ExhibitionDto.ReadRecommendedExhibitionResponse response = new ExhibitionDto.ReadRecommendedExhibitionResponse(exhibition);
            response.setThumbnail(photo.getPhotoUrl().getCloudfrontUrl());

            recommendedExhibitions.add(response);
        }

        return recommendedExhibitions;
    }
}
