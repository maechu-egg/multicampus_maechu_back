package com.multipjt.multi_pjt.crew.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.multipjt.multi_pjt.crew.dao.crew.CrewMapper;
import com.multipjt.multi_pjt.crew.domain.crew.CrewCommentsRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewCommentsResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewResponseDTO;

@Service
public class CrewService {
    
    @Autowired
    private CrewMapper crewMapper;
    
    // --------- 크루 찾기 ---------

    // 크루생성
    public void createCrew(CrewRequestDTO param) {
        System.out.println("debug>>> Service: createCrew + " + crewMapper);
        System.out.println("debug>>> Service: createCrew + " + param);
        
        // 크루 생성
        crewMapper.createCrewRow(param);
        
        // 크루장 추가
        CrewMemberRequestDTO crewMember = new CrewMemberRequestDTO();
        crewMember.setCrew_id(param.getCrew_id()); // 생성된 크루 ID
        crewMember.setMember_id(param.getMember_id()); // 크루장 ID
        crewMember.setCrew_member_state(1); // 크루장 상태

        crewMapper.insertCrewMemberRow(crewMember); // 크루 멤버 추가
    }

    // 크루 리스트 전체 조회
    public List<CrewResponseDTO> getCrewList() {
        System.out.println("debug>>> Service: getCrewList + " + crewMapper);
        return crewMapper.selectCrewRow();
    }

    // 특정 크루 리스트 조회 (종목)
    public List<CrewResponseDTO> getCrewSportList(Map<String, String> map) {
        System.out.println("debug>>> Service: getCrewSportList + " + crewMapper);
        System.out.println("debug>>> Service: getCrewSportList + " + map);
        return crewMapper.selectCrewSportRow(map);
    }

    // 특정 크루 정보 조회
    public CrewResponseDTO getCrewInfo(Integer crewId) {
        System.out.println("debug>>> Service: getCrewInfo + " + crewMapper);
        System.out.println("debug>>> Service: getCrewInfo + " + crewId);
        return crewMapper.selectCrewInfoRow(crewId);
    }

    // 크루원 신청
    public void addCrewMember(CrewMemberRequestDTO param) {
        System.out.println("debug>>> Service: addCrewMember + " + crewMapper);
        System.out.println("debug>>> Service: addCrewMember + " + param);
        crewMapper.insertCrewMemberRow(param);
    }

    // 내가 속한 크루 조회
    public List<CrewResponseDTO> getMyCrewList(Integer member_id) {
        System.out.println("debug>>> Service: getMyCrewList + " + crewMapper);
        System.out.println("debug>>> Service: getMyCrewList + " + member_id);
        return crewMapper.selectMyCrewRow(member_id);
    }

    // --------- 크루 소개 ---------

    // 크루 소개 수정
    public void updateCrewIntro(CrewRequestDTO param, int token_id) {
        System.out.println("debug>>> Service: updateCrewIntro + " + crewMapper);
        System.out.println("debug>>> Service: updateCrewIntro + " + param);
        System.out.println("debug>>> Service: updateCrewIntro + " + token_id);

        int leaderId = crewMapper.selectCrewLeaderIdRow(param.getCrew_id());

        if(token_id == leaderId) {
            crewMapper.updateCrewIntroRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루장만 크루 소개를 수정할 수 있습니다.");
        }
    }

    // 크루 관리 수정
    public void updateCrewInfo(CrewRequestDTO param, Integer token_id) {
        System.out.println("debug>>> Service: updateCrewInfo + " + crewMapper);
        System.out.println("debug>>> Service: updateCrewInfo + " + param);
        System.out.println("debug>>> Service: updateCrewInfo + " + token_id);
        
        int leaderId = crewMapper.selectCrewLeaderIdRow(param.getCrew_id());

        if(token_id == leaderId) {
            crewMapper.updateCrewInfoRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루장만 크루 관리를 수정할 수 있습니다.");
        }
    }

    // 크루 삭제
    public void deleteCrew(Integer crewId, Integer token_id) {
        System.out.println("debug>>> Service: deleteCrew + " + crewMapper);
        System.out.println("debug>>> Service: deleteCrew + " + crewId);
        System.out.println("debug>>> Service: deleteCrew + " + token_id);

        int leaderId = crewMapper.selectCrewLeaderIdRow(crewId);

        if(token_id == leaderId) {
            crewMapper.deleteCrewRow(crewId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루장만 크루를 삭제할 수 있습니다.");
        }
    }

    // --------- 크루원 정보 ---------

    // 크루원 가입 승인
    public void approveCrewMember(CrewMemberRequestDTO param, Integer token_id) {
        System.out.println("debug>>> Service: approveCrewMember + " + crewMapper);
        System.out.println("debug>>> Service: approveCrewMember + " + param);
        System.out.println("debug>>> Service: approveCrewMember + " + token_id);

        int leaderId = crewMapper.selectCrewLeaderIdRow(param.getCrew_id());
        System.out.println("token_id: " + token_id + ", leaderId: " + leaderId);

        if(token_id == leaderId) {   
            crewMapper.updateCrewMemberRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루장만 크루원 가입을 승인할 수 있습니다.");
        }
    }

    // 크루원 조회
    public List<CrewMemberResponseDTO> getCrewMemberList(Integer crewId, Integer token_id) {
        System.out.println("debug>>> Service: getCrewMemberList + " + crewMapper);
        System.out.println("debug>>> Service: getCrewMemberList + " + crewId);
        System.out.println("debug>>> Service: getCrewMemberList + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(crewId).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            return crewMapper.selectCrewMemberRow(crewId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 조회가 가능합니다.");
        }
    }

    // 크루원 삭제
    public void deleteCrewMember(CrewMemberRequestDTO param, Integer token_id) {
        System.out.println("debug>>> Service: deleteCrewMember + " + crewMapper);
        System.out.println("debug>>> Service: deleteCrewMember + " + param);
        System.out.println("debug>>> Service: deleteCrewMember + " + token_id);

        if(token_id == crewMapper.selectCrewLeaderIdRow(param.getCrew_id()) || token_id == param.getMember_id()) {
            crewMapper.deleteCrewMemberRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루장 또는 자신만 크루원을 삭제할 수 있습니다.");
        }
    }

    // --------- 크루 게시판 ---------

    // 크루 게시물 등록
    public void createCrewPost(CrewPostRequestDTO param, Integer token_id) {
        System.out.println("debug>>> Service: createCrewPost + " + crewMapper);
        System.out.println("debug>>> Service: createCrewPost + " + param);
        
        // 크루원인지, 승인 상태인지 확인
        boolean isActiveMember = crewMapper.selectCrewMemberRow(param.getCrew_id()).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            crewMapper.insertCrewPostRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 게시물 등록이 가능합니다.");
        }
    }

    // 크루 게시물 전체 조회
    public List<CrewPostResponseDTO> getCrewPostList(Integer crewId, Integer token_id) {
        System.out.println("debug>>> Service: getCrewPostList + " + crewMapper);
        System.out.println("debug>>> Service: getCrewPostList + " + crewId);
        System.out.println("debug>>> Service: getCrewPostList + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(crewId).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            return crewMapper.selectCrewPostListRow(crewId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 게시물 조회가 가능합니다.");
        }
    }

    // 크루 게시물 상단 공지, 일반 고정 3개씩
    public List<CrewPostResponseDTO> getCrewTopPostList(CrewPostRequestDTO param, Integer token_id) {
        System.out.println("debug>>> Service: getCrewTopPostList + " + crewMapper);
        System.out.println("debug>>> Service: getCrewTopPostList + " + param);
        System.out.println("debug>>> Service: getCrewTopPostList + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(param.getCrew_id()).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            return crewMapper.selectCrewTopPostRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 게시물 조회가 가능합니다.");
        }
    }

    // 크루 게시물 공지/인기/일반 조회
    public List<CrewPostResponseDTO> getCrewNoticePostList(CrewPostRequestDTO param, Integer token_id) {
        System.out.println("debug>>> Service: getCrewNoticePostList + " + crewMapper);
        System.out.println("debug>>> Service: getCrewNoticePostList + " + param);
        System.out.println("debug>>> Service: getCrewNoticePostList + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(param.getCrew_id()).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            return crewMapper.selectCrewNoticePostRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 게시물 조회가 가능합니다.");
        }
    }

    // 크루 게시물 상세 조회
    public CrewPostResponseDTO getCrewPost(CrewPostRequestDTO param) {
        System.out.println("debug>>> Service: getCrewPost + " + crewMapper);
        System.out.println("debug>>> Service: getCrewPost + " + param);
        return crewMapper.selectCrewPostRow(param);
    }

    // 크루 게시물 수정
    public void updateCrewPost(CrewPostRequestDTO param) {
        System.out.println("debug>>> Service: updateCrewPost + " + crewMapper);
        System.out.println("debug>>> Service: updateCrewPost + " + param);
        crewMapper.updateCrewPostRow(param);
    }

    // 크루 게시물 삭제
    public void deleteCrewPost(CrewPostRequestDTO param) {
        System.out.println("debug>>> Service: deleteCrewPost + " + crewMapper);
        System.out.println("debug>>> Service: deleteCrewPost + " + param);
        crewMapper.deleteCrewPostRow(param);
    }

    // --------- 크루 댓글 ---------

    // 크루 댓글 작성
    public void createCrewComment(CrewCommentsRequestDTO param) {
        System.out.println("debug>>> Service: createCrewComment + " + crewMapper);
        System.out.println("debug>>> Service: createCrewComment + " + param);
        crewMapper.insertCrewCommentRow(param);
    }

    // 크루 댓글 조회
    public List<CrewCommentsResponseDTO> getCrewCommentList(CrewCommentsRequestDTO param) {
        System.out.println("debug>>> Service: getCrewCommentList + " + crewMapper);
        System.out.println("debug>>> Service: getCrewCommentList + " + param);
        return crewMapper.selectCrewCommentsRow(param);
    }

    // 크루 댓글 삭제
    public void deleteCrewComment(CrewCommentsRequestDTO param) {
        System.out.println("debug>>> Service: deleteCrewComment + " + crewMapper);
        System.out.println("debug>>> Service: deleteCrewComment + " + param);
        crewMapper.deleteCrewCommentRow(param);
    }
}
