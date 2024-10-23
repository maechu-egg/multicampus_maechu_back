package com.multipjt.multi_pjt.badge.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.badge.service.MemberBadgeManager;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {

    private final MemberBadgeManager memberBadgeManager;

    public BadgeController(MemberBadgeManager memberBadgeManager) {
        this.memberBadgeManager = memberBadgeManager;
    }

    // 활동 처리 API (POST 요청)
    @PostMapping("/processActivity")
    public ResponseEntity<String> processActivity(
            @RequestParam("memberId") int memberId,
            @RequestParam("activityType") String activityType) {  // Add activityType as a request parameter
        memberBadgeManager.processActivity(memberId, activityType);  // Pass activityType to the service
        return ResponseEntity.ok("Processed activity for memberId: " + memberId);
    }
}
