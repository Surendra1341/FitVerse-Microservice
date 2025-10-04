package com.ssc.aiservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String activityId;
    private String userId;
    private String activityType;

    @Column(columnDefinition = "TEXT") // use TEXT instead of @Lob to avoid large object API
    private String recommendation;

    @ElementCollection // store list of strings in separate table
    @CollectionTable(name = "recommendation_improvements", joinColumns = @JoinColumn(name = "recommendation_id"))
    @Column(name = "improvement", columnDefinition = "TEXT")
    private List<String> improvements = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "recommendation_suggestions", joinColumns = @JoinColumn(name = "recommendation_id"))
    @Column(name = "suggestion", columnDefinition = "TEXT")
    private List<String> suggestions = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "recommendation_safety", joinColumns = @JoinColumn(name = "recommendation_id"))
    @Column(name = "safety_tip", columnDefinition = "TEXT")
    private List<String> safety = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;
}
