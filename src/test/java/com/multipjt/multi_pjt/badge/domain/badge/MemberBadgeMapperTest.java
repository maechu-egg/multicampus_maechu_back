package com.multipjt.multi_pjt.badge.domain.badge;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void testInsertAndSelectBadge() {
        // 1. 뱃지 생성
        MemberBadgeRequestDTO badgeRequest = new MemberBadgeRequestDTO();
        badgeRequest.setBadge_id(1);
        badgeRequest.setCurrent_points(10.5f);
        badgeRequest.setMember_id(1);
        badgeRequest.setBadge_level("Bronze");
        memberBadgeMapper.insertBadge(badgeRequest);

        // 2. 뱃지 조회
        MemberBadgeResponseDTO badgeResponse = memberBadgeMapper.selectBadgeByMemberId(1);
        assertThat(badgeResponse).isNotNull();
        assertThat(badgeResponse.getMember_id()).isEqualTo(1);
        assertThat(badgeResponse.getBadge_level()).isEqualTo("Bronze");
    }

    @Test
    public void testUpdateBadge() {
        // 1. 기존 뱃지 업데이트
        MemberBadgeRequestDTO badgeRequest = new MemberBadgeRequestDTO();
        badgeRequest.setMember_id(1);
        badgeRequest.setCurrent_points(30.0f);
        badgeRequest.setBadge_level("Silver");
        memberBadgeMapper.updateBadge(badgeRequest);

        // 2. 업데이트 후 조회
        MemberBadgeResponseDTO badgeResponse = memberBadgeMapper.selectBadgeByMemberId(1);
        assertThat(badgeResponse.getBadge_level()).isEqualTo("Silver");
        assertThat(badgeResponse.getCurrent_points()).isEqualTo(30.0f);
    }
}
