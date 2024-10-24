package com.multipjt.multi_pjt.badge.ctrl;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.badge.service.BadgeService;
import com.multipjt.multi_pjt.badge.service.MemberBadgeManager;
import com.multipjt.multi_pjt.community.domain.UserActivity.UserActivityRequestDTO;
import com.multipjt.multi_pjt.community.domain.UserActivity.UserActivityResponseDTO;

@RestController
@RequestMapping("/badges")
public class BadgeController {

    private final MemberBadgeManager memberBadgeManager;
    private final BadgeService badgeService;

    // BadgeController 생성자
    public BadgeController(MemberBadgeManager memberBadgeManager, BadgeService badgeService) {
        this.memberBadgeManager = memberBadgeManager;
        this.badgeService = badgeService;
    }

    //엔드포인트
    @PostMapping("/processActivity")
    public ResponseEntity<Map<String, Object>> processActivity(@RequestBody UserActivityRequestDTO request) {
        try {
            // 활동을 처리하고 현재 포인트와 뱃지 레벨을 가져옴
            int currentPoints = memberBadgeManager.processActivity(request.getMember_id(), request.getUser_activity());
            String badgeLevel = badgeService.getBadgeLevel(currentPoints);

            UserActivityResponseDTO response = new UserActivityResponseDTO();
            response.setUser_activity_id(request.getUser_activity_id());
            response.setUser_activity(request.getUser_activity());
            response.setActivity_date(request.getActivity_date());
            response.setPost_id(request.getPost_id());
            response.setMember_id(request.getMember_id());

            // 성공 
            return ResponseEntity.ok(Map.of(
                "message", String.format("Processed activity: %s for memberId: %d", request.getUser_activity(), request.getMember_id()),
                "currentPoints", currentPoints,
                "badgeLevel", badgeLevel,
                "activityResponse", response
            ));
        } catch (IllegalArgumentException e) {
            // 잘못된 요청에 대한 응답 반환
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // 예기치 않은 오류에 대한 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "예기치 않은 오류가 발생했습니다"));
        }
    }

    // 전역 예외 처리 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "예기치 않은 오류가 발생했습니다"));
    }
}
