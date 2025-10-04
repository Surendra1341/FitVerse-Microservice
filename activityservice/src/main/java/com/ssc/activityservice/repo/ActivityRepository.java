package com.ssc.activityservice.repo;

import com.ssc.activityservice.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,String> {
    List<Activity> findByUserId(String userId);
}
