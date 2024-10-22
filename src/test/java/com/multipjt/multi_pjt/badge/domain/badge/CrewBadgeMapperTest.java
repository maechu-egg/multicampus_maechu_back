package com.multipjt.multi_pjt.badge.domain.badge;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.multipjt.multi_pjt.Badge.dao.CrewBadgeMapper;

import jakarta.transaction.Transactional;
import com.multipjt.multi_pjt.Badge.domain.badge.CrewBadgeRequestDTO;
import com.multipjt.multi_pjt.Badge.domain.badge.CrewBadgeResponseDTO;


@SpringBootTest
@Transactional
public class CrewBadgeMapperTest {

    @Autowired
    private CrewBadgeMapper crewBadgeMapper;

    @Test
    public void testInsertAndSelectBadge() {
        // 1. 뱃지 생성
        CrewBadgeRequestDTO badgeRequest = new CrewBadgeRequestDTO();
        badgeRequest.setCrew_badge_id(1);
        badgeRequest.setCrew_current_points(10.5f);
        badgeRequest.setMember_id(1);
        badgeRequest.setBadge_level("Bronze");
        crewBadgeMapper.insertBadge(badgeRequest);

        // 2. 뱃지 조회
        CrewBadgeResponseDTO badgeResponse = crewBadgeMapper.selectBadgeByMemberId(1);
        assertThat(badgeResponse).isNotNull();
        assertThat(badgeResponse.getMember_id()).isEqualTo(1);
        assertThat(badgeResponse.getBadge_level()).isEqualTo("Bronze");
    }

    
    
}
