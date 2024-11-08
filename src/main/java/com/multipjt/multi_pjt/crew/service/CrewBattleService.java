package com.multipjt.multi_pjt.crew.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.multipjt.multi_pjt.crew.dao.battle.CrewBattleMapper;
import com.multipjt.multi_pjt.crew.dao.crew.CrewMapper;
import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewVoteRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedResponseDTO;

@Service
public class CrewBattleService {
    @Autowired
    private CrewBattleMapper crewBattleMapper;

    @Autowired
    private CrewMapper crewMapper;

    // <---- 크루 배틀 ---->

    // 배틀 생성
    public void createCrewBattle(CrewBattleRequestDTO param, Integer token_id) {
        System.out.println("debug>>> Service: createCrewBattle + " + crewBattleMapper);
        System.out.println("debug>>> Service: createCrewBattle + " + param);
        System.out.println("debug>>> Service: createCrewBattle + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(param.getCrew_id()).stream()
        .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);
        
        if (isActiveMember) {
            crewBattleMapper.createCrewBattleRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 배틀 생성이 가능합니다.");
        }
    }

    // 배틀 목록 조회
    public List<CrewBattleResponseDTO> selectCrewBattle(Integer crew_id, Integer token_id) {
        System.out.println("debug>>> Service: selectCrewBattle + " + crewBattleMapper);
        System.out.println("debug>>> Service: selectCrewBattle + " + crew_id);
        System.out.println("debug>>> Service: selectCrewBattle + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(crew_id).stream()
        .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            return crewBattleMapper.selectCrewBattleRow(crew_id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 배틀 목록 조회가 가능합니다.");
        }
    }

    
    // 특정 배틀 상세 조회
    public CrewBattleResponseDTO selectCrewBattleDetail(Integer crew_id, Integer battle_id, Integer token_id) {
        System.out.println("debug>>> Service: selectCrewBattleDetail + " + crewBattleMapper);
        System.out.println("debug>>> Service: selectCrewBattleDetail + " + crew_id);
        System.out.println("debug>>> Service: selectCrewBattleDetail + " + battle_id);
        System.out.println("debug>>> Service: selectCrewBattleDetail + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(crew_id).stream()
        .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            return crewBattleMapper.selectCrewBattleDetailRow(battle_id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 배틀 상세 조회가 가능합니다.");
        }
    }

    // 사용자 참여 배틀 조회
    public List<CrewBattleResponseDTO> selectMyBattle(int member_id) {
        System.out.println("debug>>> Service: selectMyBattle + " + member_id);
        List<CrewBattleResponseDTO> battles = crewBattleMapper.selectMyBattleRow(member_id);
        if (battles == null || battles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "참여한 배틀이 없습니다.");
        }
        return battles;
    }

    // 배틀 참가
    public void createBattleMember(BattleMemberRequestDTO params, Integer token_id) {
        System.out.println("debug>>> Service: createBattleMember + " + crewBattleMapper);
        System.out.println("debug>>> Service: createBattleMember + " + params);
        System.out.println("debug>>> Service: createBattleMember + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(params.getCrew_id()).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            try {
                crewBattleMapper.insertBattleMemberRow(params);
            } catch (DuplicateKeyException e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 참가한 배틀입니다.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 배틀 참가가 가능합니다.");
        }
    }

    // <---- 배틀 피드보기 ---->

    // 배틀 참가 멤버 조회
    public List<BattleMemberResponseDTO> selectBattleMember(Integer crew_id, Integer battle_id, Integer token_id) {
        System.out.println("debug>>> Service: selectBattleMember + " + crewBattleMapper);
        System.out.println("debug>>> Service: selectBattleMember + " + crew_id);
        System.out.println("debug>>> Service: selectBattleMember + " + battle_id);
        System.out.println("debug>>> Service: selectBattleMember + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(crew_id).stream()
        .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            return crewBattleMapper.selectBattleMemberRow(battle_id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 배틀 참가 멤버 조회가 가능합니다.");
        }
    }

    // 피드 작성
    public void createCrewBattleFeed(CrewBattleFeedRequestDTO param, int token_id) {
        System.out.println("debug>>> Service: createCrewBattleFeed + " + crewBattleMapper);
        System.out.println("debug>>> Service: createCrewBattleFeed + " + param);
        System.out.println("debug>>> Service: createCrewBattleFeed + " + token_id);

        boolean isBattleMember = crewBattleMapper.selectBattleMemberRow(param.getBattle_id()).stream()
            .anyMatch(member -> member.getMember_id() == token_id);

        if (isBattleMember) {
            if (param.getMember_id() == token_id) {
                crewBattleMapper.createCrewBattleFeedRow(param);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "자신의 피드만 작성이 가능합니다.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "배틀 참가 크루원만 피드 작성이 가능합니다.");
        }
    }


    // 피드 조회
    public List<CrewBattleFeedResponseDTO> selectCrewBattleFeed(int crew_id, int param, int token_id) {
        System.out.println("debug>>> Service: selectCrewBattleFeed + " + crewBattleMapper);
        System.out.println("debug>>> Service: selectCrewBattleFeed + " + crew_id);
        System.out.println("debug>>> Service: selectCrewBattleFeed + " + param);
        System.out.println("debug>>> Service: selectCrewBattleFeed + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(crew_id).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            return crewBattleMapper.selectCrewBattleFeedRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 피드 조회가 가능합니다.");
        }
    }

    // 투표
    public void createVote(CrewVoteRequestDTO params, int token_id, int crew_id) {
        System.out.println("debug>>> Service: createVote + " + crewBattleMapper);
        System.out.println("debug>>> Service: createVote + " + params);
        System.out.println("debug>>> Service: createVote + " + token_id);
        System.out.println("debug>>> Service: createVote + " + crew_id);
        
        // 크루원 확인
        boolean isActiveMember = crewMapper.selectCrewMemberRow(crew_id).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (!isActiveMember) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 투표가 가능합니다.");
        }

        // 배틀 종료일 확인
        LocalDateTime battleEndDate = crewBattleMapper.selectCrewBattleDetailRow(params.getBattle_id()).getBattle_end_date();
        LocalDateTime votingStartDate = battleEndDate.minusDays(7);

        if (LocalDateTime.now().isBefore(votingStartDate)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "투표는 배틀 종료일 7일 전부터 가능합니다.");
        }

        // 투표 생성
        crewBattleMapper.createVoteRow(params);
    }
}