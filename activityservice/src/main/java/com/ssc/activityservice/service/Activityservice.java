package com.ssc.activityservice.service;

import com.netflix.discovery.converters.Auto;
import com.ssc.activityservice.dto.ActivityRequest;
import com.ssc.activityservice.dto.ActivityResponse;
import com.ssc.activityservice.model.Activity;
import com.ssc.activityservice.model.ActivityType;
import com.ssc.activityservice.repo.ActivityRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class Activityservice {

    @Autowired
    private  ActivityRepository activityRepository;

    @Autowired
    private  UserValidationService userValidationService;


    @Autowired
    private  RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;


    @Value("${rabbitmq.routing.key}")
    private String routingKey;



    public ActivityResponse trackActivity(ActivityRequest request) {
        // validation
        boolean isValidUser = userValidationService.validateUser(request.getUserId());
        if(!isValidUser){
            throw new RuntimeException("Invalid user id :"+request.getUserId());
        }
        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);


        // publish to RabbitMq for AI response
        try{
            rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);
        }catch(Exception e){
            System.out.printf("Exception occurred while sending activity to RabbitMQ : %s\n",e.getMessage());
        }

        return mapToResponse(savedActivity);

    }

    private ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());

        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());

        return response;
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);

        return activities.stream().map( this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ActivityResponse getActivity(String activityId) {
        return  mapToResponse(activityRepository.findById(activityId).orElseThrow(()-> new RuntimeException("Activity not found!")));
    }
}
