package com.multipjt.multi_pjt.badge.domain.badge;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.multipjt.multi_pjt.badge.dao.MemberBadgeMapper;
import com.multipjt.multi_pjt.badge.dao.UserActivityRecordMapper;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class MemberBadgeMapperTest {

    @Autowired
    private MemberBadgeMapper memberBadgeMapper;

    @Autowired
    private UserActivityRecordMapper userActivityRecordMapper;

    @Test
    public void testInsertBadge() {
        // 뱃지 생성 테스트
        MemberBadgeRequestDTO newBadge = new MemberBadgeRequestDTO();
        newBadge.setBadge_id(1); 
        newBadge.setCurrent_points(10.0f);
        newBadge.setMember_id(1); 
        newBadge.setBadge_level("Bronze");
        
        memberBadgeMapper.insertBadge(newBadge);
        System.out.println("뱃지 생성 완료");
    }

    @Test
    public void testSelectBadgeByMemberId() {
        // 뱃지 조회 테스트
        int memberId = 1; 
        MemberBadgeResponseDTO badge = memberBadgeMapper.getBadgeByMemberId(memberId);
        
        if (badge != null) {
            System.out.println("뱃지 조회 성공: " + badge.getBadge_level());
            System.out.println("현재 포인트: " + badge.getCurrent_points());
        } else {
            System.out.println("뱃지를 찾을 수 없습니다.");
        }
    }

    @Test
    public void testUpdateBadge() {
        // 뱃지 업데이트 테스트
        MemberBadgeRequestDTO updateBadge = new MemberBadgeRequestDTO();
        updateBadge.setBadge_id(1); 
        updateBadge.setCurrent_points(30.0f); 
        updateBadge.setMember_id(1); 
        updateBadge.setBadge_level("Silver"); 
        
        memberBadgeMapper.updateBadge(updateBadge);
        System.out.println("뱃지 업데이트 완료");
    }

    @Test
    public void testInsertActivityAndUpdatePoints() {
        // 테스트할 데이터 설정
        String activityType = "diet"; // 예시 활동 유형
        int memberId = 140; // 예시 회원 ID

        // 파라미터 맵 생성
        Map<String, Object> params = new HashMap<>();
        params.put("activityType", activityType);
        params.put("memberId", memberId);

        // 스토어드 프로시저 호출
        userActivityRecordMapper.insertActivityAndUpdatePoints(params);

        // 결과 확인 (예: MemberBadge 테이블에서 점수 확인)
        BigDecimal currentPoints = memberBadgeMapper.getCurrentPoints(memberId);
        assertNotNull(currentPoints); // 점수가 null이 아닌지 확인
        System.out.println("Current Points: " + currentPoints);
    }

}