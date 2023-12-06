package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.entity.*;
import com.blackshoe.esthetecoreservice.exception.PhotoErrorResult;
import com.blackshoe.esthetecoreservice.exception.PhotoException;
import com.blackshoe.esthetecoreservice.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
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
        Set<Long> userGenres = userPreferredGenres.getOrDefault(userId, new HashSet<>());
        Set<Long> exhibitionGenreIds = exhibitionGenres.getOrDefault(exhibitionId, new HashSet<>());
        userGenres.retainAll(exhibitionGenreIds);
        return userGenres.size();
    }

    private DataModel createDataModel() {
        List<View> views = viewRepository.findAll();
        Map<Long, Set<Long>> userPreferredGenres = collectUserPreferredGenres();
        Map<Long, Set<Long>> exhibitionGenres = collectExhibitionGenres();

        FastByIDMap<PreferenceArray> userData = new FastByIDMap<>();

        for (View view : views) {
            long userId = view.getUser().getId();
            long exhibitionId = view.getExhibition().getId();
            float preferenceValue = calculatePreferenceScore(userId, exhibitionId, userPreferredGenres, exhibitionGenres);

            List<GenericPreference> userPreferences = new ArrayList<>();
            if (userData.containsKey(userId)) {
                PreferenceArray prefsArray = userData.get(userId);
                for (int i = 0; i < prefsArray.length(); i++) {
                    Preference pref = prefsArray.get(i);
                    userPreferences.add(new GenericPreference(pref.getUserID(), pref.getItemID(), pref.getValue()));
                }
            }

            userPreferences.add(new GenericPreference(userId, exhibitionId, preferenceValue));
            userData.put(userId, new GenericUserPreferenceArray(userPreferences));
        }

        return new GenericDataModel(userData);
    }

    public List<RecommendedItem> recommendExhibitions(UUID userId, int numberOfRecommendations) throws Exception {
        long userLongId = getUserIdFromUUID(userId);
        DataModel model = createDataModel();

        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

        return recommender.recommend(userLongId, numberOfRecommendations);
    }

    private long getUserIdFromUUID(UUID userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new IllegalStateException("User not found")).getId();
    }

    @Override
    public List<ExhibitionDto.ReadRecommendedExhibitionResponse> getRecommendedExhibitions(UUID userId) throws Exception {
        List<RecommendedItem> recommendedItems = recommendExhibitions(userId, 10);
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
