package com.multipjt.multi_pjt.badge.service;

import org.springframework.stereotype.Service;

@Service
public class ActivityPointService {

    public float calculateActivityPoints(String activityType) {
        switch (activityType) {
            case "post":
            case "comment":
            case "diet":
            case "exercise":
                return 0.5f;
            default:
                return 0f;
        }
    }
}

