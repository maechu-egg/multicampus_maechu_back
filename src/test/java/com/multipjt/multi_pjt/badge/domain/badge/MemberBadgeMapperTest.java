package com.multipjt.multi_pjt.badge.domain.badge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.multipjt.multi_pjt.badge.dao.MemberBadgeMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class MemberBadgeMapperTest {

    @Autowired
    private MemberBadgeMapper memberBadgeMapper;

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


}