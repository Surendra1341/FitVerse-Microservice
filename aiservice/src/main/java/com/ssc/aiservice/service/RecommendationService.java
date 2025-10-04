package com.ssc.aiservice.service;


import com.ssc.aiservice.model.Recommendation;
import com.ssc.aiservice.repo.RecommendationRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RecommendationService {

    private final RecommendationRepo recommendationRepo;

    public List<Recommendation> getUserRecommendation(String userId) {
        return recommendationRepo.findByUserId(userId);
    }

    public Recommendation getActivityRecommendation(String activityId) {
        return recommendationRepo.findByActivityId(activityId).orElseThrow(
                () -> new RuntimeException("activityId not found")
        );
    }
}
