package com.multipjt.multi_pjt.crew.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;  

import com.multipjt.multi_pjt.crew.dao.crew.CrewMapper;
import com.multipjt.multi_pjt.crew.domain.crew.CrewCommentsRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewCommentsResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewResponseDTO;

public class CrewService {
    
    @Autowired
    private CrewMapper crewMapper;
    
    // 크루생성
    public void createCrew(CrewRequestDTO param) {
        crewMapper.createCrewRow(param);
    }

    // 전체 크루 리스트 조회
    public List<CrewResponseDTO> getCrewList() {
        return crewMapper.selectCrewRow();
    }

    // 특정 크루 리스트 조회 (종목)
    public List<CrewResponseDTO> getCrewSportList(Map<String, String> map) {
        return crewMapper.selectCrewSportRow(map);
    }

    // 특정 크루 정보 조회
    public CrewResponseDTO getCrewInfo(Map<String, Integer> map) {
        return crewMapper.selectCrewInfoRow(map);
    }

    // 크루원 추가
    public void addCrewMember(CrewMemberRequestDTO param) {
        crewMapper.insertCrewMemberRow(param);
    }

    // --------- 크루 소개 ---------

    // 크루 소개 수정
    public void updateCrewIntro(CrewRequestDTO param) {
        crewMapper.updateCrewIntroRow(param);
    }

    // 크루 관리 수정
    public void updateCrewInfo(CrewRequestDTO param) {
        crewMapper.updateCrewInfoRow(param);
    }

    // 크루 삭제
    public void deleteCrew(CrewRequestDTO param) {
        crewMapper.deleteCrewRow(param);
    }

    // --------- 크루원 정보 ---------

    // 크루원 가입 승인
    public void approveCrewMember(CrewMemberRequestDTO param) {
        crewMapper.updateCrewMemberRow(param);
    }

    // 크루원 조회
    public List<CrewMemberResponseDTO> getCrewMemberList(Map<String, Integer> map) {
        return crewMapper.selectCrewMemberRow(map);
    }

    // 크루원 삭제
    public void deleteCrewMember(CrewMemberRequestDTO param) {
        crewMapper.deleteCrewMemberRow(param);
    }

    // --------- 크루 게시판 ---------

    // 크루 게시물 등록
    public void createCrewPost(CrewPostRequestDTO param) {
        crewMapper.insertCrewPostRow(param);
    }

    // 크루 게시물 전체 조회
    public List<CrewPostResponseDTO> getCrewPostList(Map<String, Integer> map) {
        return crewMapper.selectCrewPostListRow(map);
    }

    // 크루 게시물 상단 공지, 일반 고정 3개씩
    public List<CrewPostResponseDTO> getCrewTopPostList(CrewPostRequestDTO param) {
        return crewMapper.selectCrewTopPostRow(param);
    }

    // 크루 게시물 공지/인기/일반 조회
    public List<CrewPostResponseDTO> getCrewNoticePostList(CrewPostRequestDTO param) {
        return crewMapper.selectCrewNoticePostRow(param);
    }

    // 크루 게시물 상세 조회
    public CrewPostResponseDTO getCrewPostDetail(CrewPostRequestDTO param) {
        return crewMapper.selectCrewPostRow(param);
    }

    // 크루 게시물 수정
    public void updateCrewPost(CrewPostRequestDTO param) {
        crewMapper.updateCrewPostRow(param);
    }

    // --------- 크루 댓글 ---------
    
    // 크루 댓글 작성
    public void createCrewComment(CrewCommentsRequestDTO param) {
        crewMapper.insertCrewCommentRow(param);
    }

    // 크루 댓글 조회
    public List<CrewCommentsResponseDTO> getCrewCommentList(CrewCommentsRequestDTO param) {
        return crewMapper.selectCrewCommentsRow(param);
    }

    // 크루 댓글 삭제
    public void deleteCrewComment(CrewCommentsRequestDTO param) {
        crewMapper.deleteCrewCommentRow(param);
    }
}
