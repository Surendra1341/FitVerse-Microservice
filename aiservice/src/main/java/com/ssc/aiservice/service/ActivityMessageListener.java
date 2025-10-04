package com.ssc.aiservice.service;

import com.ssc.aiservice.config.RabbitMqConfig;
import com.ssc.aiservice.model.Activity;
import com.ssc.aiservice.model.Recommendation;
import com.ssc.aiservice.repo.RecommendationRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActivityMessageListener {


    @Autowired
    private ActivityAiService activityAiService;

    @Autowired
     private RecommendationRepo  recommendationRepo;

    @RabbitListener(queues = "activity.queue" )
    public void processActivity(Activity activity){
        log.info("Processing activity {}", activity.getId());
//        log.info("Generated Recommendation : {}",);
        Recommendation recommendation =activityAiService.generateRecommendation(activity);
        recommendationRepo.save(recommendation);
    }
}
