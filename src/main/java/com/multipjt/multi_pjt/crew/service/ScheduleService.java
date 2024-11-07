package com.multipjt.multi_pjt.crew.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.crew.dao.crew.CrewMapper;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostResponseDTO;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private CrewMapper crewMapper;
    
    // 크루 게시물 상태 인기 변경 스케줄러
    @Scheduled(cron = "0 0 0/1 * * *")
    public void updatePostStatusIfPopular() {
        List<CrewPostResponseDTO> posts = crewMapper.selectPopularPostRow();
        for (CrewPostResponseDTO post : posts) {
            updatePostStatusIfPopular(post.getCrew_post_id(), post.getCrew_id());
        }
    }

    // 크루 게시물 상태 인기 변경
    public void updatePostStatusIfPopular(int post_id, int crew_id) {
        System.out.println("debug>>> Service: updatePostStatusIfPopular 게시물 id+ " + post_id);
        System.out.println("debug>>> Service: updatePostStatusIfPopular 크루 id+ " + crew_id);

        int memberCount = crewMapper.selectCrewMemberCountRow(crew_id);
        int likeCount = crewMapper.selectPostLikeCountRow(post_id);
        System.out.println("debug>>> Service: updatePostStatusIfPopular 크루 멤버수+ " + memberCount);
        System.out.println("debug>>> Service: updatePostStatusIfPopular 게시물 좋아요수+ " + likeCount);

        if(likeCount >= Math.ceil(memberCount / 2.0)) {
            crewMapper.updatePostStatusRow(post_id);
        }
    }
}
