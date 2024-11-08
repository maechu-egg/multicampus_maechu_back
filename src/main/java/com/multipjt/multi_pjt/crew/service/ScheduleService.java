package com.multipjt.multi_pjt.crew.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.crew.dao.crew.CrewMapper;
import com.multipjt.multi_pjt.crew.dao.battle.CrewBattleMapper;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewVoteResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleRequestDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@EnableAsync
public class ScheduleService {

    @Autowired
    private CrewMapper crewMapper;

    @Autowired
    private CrewBattleMapper crewBattleMapper;
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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
            System.out.println("debug>>> Service: updatePostStatusIfPopular 게시물 상태 변경 완료");
        }
    }

    // 배틀 상태 변경 및 자동 삭제 스케줄러
    // @Scheduled(cron = "*/10 * * * * *")
    @Scheduled(cron = "0 0 */1 * * *") // 매 1시간마다 실행
    public void updateBattleStatus() {
        List<CrewBattleResponseDTO> battles = crewBattleMapper.selectAllCrewBattleRow();
        System.out.println("debug>>> Service: updateBattleStatus 배틀 조회 완료" + battles);
        for (CrewBattleResponseDTO battle : battles) {
            CrewBattleRequestDTO requestDTO = new CrewBattleRequestDTO();
            requestDTO.setBattle_id(battle.getBattle_id());
            requestDTO.setBattle_state(battle.getBattle_state());
            requestDTO.setBattle_end_recruitment(battle.getBattle_end_recruitment());
            requestDTO.setBattle_end_date(battle.getBattle_end_date());
            System.out.println("debug>>> Service: updateBattleStatus 배틀 조회 완료" + requestDTO);
            System.out.println("debug>>> Service: updateBattleStatus 배틀 id+ " + requestDTO.getBattle_id());
            System.out.println("debug>>> Service: updateBattleStatus 배틀 상태+ " + requestDTO.getBattle_state());

            if (battle.getBattle_state() == 0 && isRecruitmentEnded(requestDTO)) {
                requestDTO.setBattle_state(1);
                System.out.println("debug>>> Service: updateBattleStatus 배틀 상태 변경 완료"+requestDTO.getBattle_state());
                crewBattleMapper.updateBattleState(requestDTO);
            } else if (battle.getBattle_state() == 1 && isBattleEnded(requestDTO)) {
                requestDTO.setBattle_state(2);
                System.out.println("debug>>> Service: updateBattleStatus 배틀 상태 변경 완료"+requestDTO.getBattle_state());
                crewBattleMapper.updateBattleState(requestDTO);
                scheduleBattleDeletion(requestDTO);
                System.out.println("debug>>> Service: updateBattleStatus 배틀 삭제 예약 완료");
                updateWinnerPoints(requestDTO);
                System.out.println("debug>>> Service: updateBattleStatus 승리 포인트 업데이트 완료");
            }
        }
    }

    // 모집 종료일 확인
    private boolean isRecruitmentEnded(CrewBattleRequestDTO battle) {
        return battle.getBattle_end_recruitment().isBefore(LocalDateTime.now());
    }

    // 배틀 종료일 확인
    private boolean isBattleEnded(CrewBattleRequestDTO battle) {
        return battle.getBattle_end_date().isBefore(LocalDateTime.now());
    }

    // 배틀 자동 삭제 예약
    @Async
    private void scheduleBattleDeletion(CrewBattleRequestDTO battle) {
        scheduler.schedule(() -> {
            try {
                // 배틀 삭제
                crewBattleMapper.deleteBattleById(battle.getBattle_id());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 10, TimeUnit.SECONDS);
    }

    // 승리 포인트 업데이트
    private void updateWinnerPoints(CrewBattleRequestDTO battle) {
        System.out.println("debug>>> Service: updateWinnerPoints 배틀 id+ " + battle.getBattle_id());

        // 특정 배틀에 대한 모든 투표를 가져옴
        List<CrewVoteResponseDTO> votes = crewBattleMapper.selectCrewVoteRow(battle.getBattle_id());

        // 투표 수 집계
        Map<Integer, Long> voteCountMap = votes.stream()
            .collect(Collectors.groupingBy(CrewVoteResponseDTO::getParticipant_id, Collectors.counting()));

        // 최다 투표 수 찾기
        long maxVotes = voteCountMap.values().stream()
            .max(Long::compare)
            .orElse(0L);

        // 공동 우승자 찾기
        List<Integer> winnerParticipantIds = voteCountMap.entrySet().stream()
            .filter(entry -> entry.getValue() == maxVotes)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        if (!winnerParticipantIds.isEmpty()) {
            List<BattleMemberResponseDTO> battleMembers = crewBattleMapper.selectBattleMemberRow(battle.getBattle_id());

            for (Integer winnerParticipantId : winnerParticipantIds) {
                battleMembers.stream()
                    .filter(member -> member.getParticipant_id().equals(winnerParticipantId))
                    .findFirst()
                    .ifPresent(winner -> {
                        crewBattleMapper.updateWinnerPointsRow(winner.getMember_id());
                        crewBattleMapper.updateBadgeWinnerPointsRow(winner.getMember_id());
                    });
            }
        }
    }
}
