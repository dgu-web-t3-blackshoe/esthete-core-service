package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.entity.*;
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
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final RedisTemplate<String, DataModel> dataModelRedisTemplate;

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

    // 전시회별 장르 데이터 수집
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
    // 선호도 점수 계산
    private float calculatePreferenceScore(long userId, long exhibitionId,
                                           Map<Long, Set<Long>> userPreferredGenres,
                                           Map<Long, Set<Long>> exhibitionGenres) {
        Set<Long> userGenres = userPreferredGenres.getOrDefault(userId, new HashSet<>());
        Set<Long> exhibitionGenreIds = exhibitionGenres.getOrDefault(exhibitionId, new HashSet<>());

        userGenres.retainAll(exhibitionGenreIds); // 교집합 계산
        return userGenres.size(); // 교집합의 크기를 선호도 점수로 사용
    }

    @Override
    @Scheduled(cron = "0 0 6 * * *")
    public void buildDataModel() {
        List<View> views = viewRepository.findAllWithUserAndExhibition();
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
                    userPreferences.add((GenericPreference) prefsArray.get(i));
                }
            }

            userPreferences.add(new GenericPreference(userId, exhibitionId, preferenceValue));
            PreferenceArray prefs = new GenericUserPreferenceArray(userPreferences);
            userData.put(userId, prefs);
        }

        cacheDataModel(new GenericDataModel(userData));
    }
    public void cacheDataModel(DataModel dataModel) {
        ValueOperations<String, DataModel> valueOps = dataModelRedisTemplate.opsForValue();
        valueOps.set("recommendation", dataModel);
    }
    public DataModel getCachedDataModel() {
        ValueOperations<String, DataModel> valueOps = dataModelRedisTemplate.opsForValue();
        return valueOps.get("recommendation");
    }

    public List<RecommendedItem> recommendExhibitions(UUID userId, int numberOfRecommendations) throws Exception {
        long userLongId = getUserIdFromUUID(userId); // UUID를 데이터베이스 ID로 변환
        DataModel model = getCachedDataModel(); // Redis에서 캐싱된 데이터 모델 가져오기

        // 사용자 유사성 계산
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

        // 사용자 이웃 계산
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);

        // 추천기 생성
        Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

        // 사용자에 대한 추천 생성
        return recommender.recommend(userLongId, numberOfRecommendations);
    }

    private long getUserIdFromUUID(UUID userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        return user.get().getId();
    }

    //ResponseDto로 Refactoring
    @Override
    public List<ExhibitionDto.ReadRecommendedExhibitionResponse> getRecommendedExhibitions(UUID userId) throws Exception {
        List<RecommendedItem> recommendedItems = recommendExhibitions(userId, 10);
        List<ExhibitionDto.ReadRecommendedExhibitionResponse> recommendedExhibitions = new ArrayList<>();

        for (RecommendedItem item : recommendedItems) {
            long exhibitionId = item.getItemID();

            Optional<Exhibition> opExhibition = exhibitionRepository.findById(exhibitionId);

            ExhibitionDto.ReadRecommendedExhibitionResponse recommendedExhibition = new ExhibitionDto.ReadRecommendedExhibitionResponse(opExhibition.get());

            recommendedExhibitions.add(recommendedExhibition);
        }

        return recommendedExhibitions;
    }

}

