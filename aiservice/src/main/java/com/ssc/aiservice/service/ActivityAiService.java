package com.ssc.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssc.aiservice.model.Activity;
import com.ssc.aiservice.model.Recommendation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ActivityAiService {

    @Autowired
    private GemmaService gemmaService;

    public Recommendation generateRecommendation(Activity activity) {
        String prompt  = createPromptForActivity(activity);
        String response = gemmaService.processContent(prompt);
//        log.info("Response from AI: {} ",response);
        return processAiResponse(activity,response);
    }

    private Recommendation processAiResponse(Activity activity, String aiResponse) {
        try {
            // Clean markdown wrappers if present
            String cleanedResponse = aiResponse.trim();
            if (cleanedResponse.startsWith("```")) {
                // remove ```json or ```
                cleanedResponse = cleanedResponse.replaceAll("^```[a-zA-Z]*\\n?", "");
                // remove ending ```
                cleanedResponse = cleanedResponse.replaceAll("```$", "").trim();
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(cleanedResponse);

            Recommendation recommendation = new Recommendation();
            recommendation.setActivityId(activity.getId());
            recommendation.setUserId(activity.getUserId());
            recommendation.setActivityType(activity.getType().toString());

            // analysis.overall -> recommendation
            String overall = root.path("analysis").path("overall").asText();
            recommendation.setRecommendation(overall);

            // improvements
            List<String> improvements = new ArrayList<>();
            if (root.has("improvements") && root.get("improvements").isArray()) {
                for (JsonNode imp : root.get("improvements")) {
                    String area = imp.path("area").asText();
                    String rec = imp.path("recommendation").asText();
                    improvements.add(area + ": " + rec);
                }
            }
            recommendation.setImprovements(improvements);

            // suggestions
            List<String> suggestions = new ArrayList<>();
            if (root.has("suggestions") && root.get("suggestions").isArray()) {
                for (JsonNode sug : root.get("suggestions")) {
                    String workout = sug.path("workout").asText();
                    String desc = sug.path("description").asText();
                    String duration = sug.has("duration") ? " (Duration: " + sug.path("duration").asText() + ")" : "";
                    suggestions.add(workout + ": " + desc + duration);
                }
            }
            recommendation.setSuggestions(suggestions);

            // safety
            List<String> safety = new ArrayList<>();
            if (root.has("safety")) {
                if (root.get("safety").isArray()) {
                    for (JsonNode s : root.get("safety")) {
                        safety.add(s.asText());
                    }
                } else {
                    safety.add(root.get("safety").asText());
                }
            }
            recommendation.setSafety(safety);

            recommendation.setCreatedAt(LocalDateTime.now());
            return recommendation;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return createDefaultRecommendation(activity);
    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        Recommendation recommendation = new Recommendation();
        recommendation.setActivityId(activity.getId());
        recommendation.setUserId(activity.getUserId());
        recommendation.setActivityType(activity.getType().toString());

        // fallback text
        recommendation.setRecommendation("No detailed AI analysis available. Please try again later or consult a trainer.");

        // fallback improvements
        recommendation.setImprovements(List.of(
                "Maintain consistency in workouts",
                "Ensure proper warm-up and cool-down"
        ));

        // fallback suggestions
        recommendation.setSuggestions(List.of(
                "Basic Cardio: 20–30 minutes of light running, cycling, or swimming",
                "Strength Training: 2–3 sets of bodyweight exercises like push-ups, squats, lunges"
        ));

        // fallback safety
        recommendation.setSafety(List.of(
                "Stay hydrated before, during, and after activity",
                "Avoid overtraining — listen to your body",
                "Warm-up before starting and cool down afterward"
        ));

        // set created time manually (since this is default case)
        recommendation.setCreatedAt(LocalDateTime.now());

        return recommendation;
    }



    private String createPromptForActivity(Activity activity) {
        return String.format("""
        You are a professional fitness coach and data analyst. 
        Your task is to analyze the given fitness activity and return structured insights ONLY in the following EXACT JSON format (no extra text, no explanations):

        {
          "analysis": {
            "overall": "Comprehensive overview of the activity performance, considering duration, intensity, and metrics.",
            "pace": "Analysis of speed/pace trends based on the type of activity and duration.",
            "heartRate": "Analysis of cardiovascular effort and intensity level (use metrics if provided, otherwise infer).",
            "caloriesBurned": "Analysis of calories burned relative to activity type, duration, and user effort."
          },
          "improvements": [
            {
              "area": "Specific area for improvement (e.g., endurance, consistency, form)",
              "recommendation": "Detailed, actionable recommendation for improvement."
            }
          ],
          "suggestions": [
            {
              "workout": "Suggested workout name (e.g., Interval Run, Strength Training, Yoga)",
              "description": "Detailed explanation of why this workout helps and how it benefits the user’s fitness goal."
            }
          ],
          "safety": [
            "Clear safety guideline related to warm-up, hydration, posture, or recovery.",
            "Additional safety advice tailored to the activity type and intensity."
          ]
        }

        Activity details to analyze:
        - Activity Type: %s
        - Duration: %d minutes
        - Calories Burned: %d
        - Start Time: %s
        - Additional Metrics: %s
        - Created At: %s
        - Updated At: %s

        Focus your analysis on:
        1. Performance evaluation
        2. Areas of improvement
        3. Next workout suggestions
        4. Safety guidelines

        Remember: The response MUST strictly follow the JSON format shown above.
        """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getStartTime(),
                activity.getAdditionalMetrics(),
                activity.getCreatedAt(),
                activity.getUpdatedAt()
        );
    }
}
