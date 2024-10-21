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
    
    // --------- 크루 찾기 ---------

    // 크루생성
    public void createCrew(CrewRequestDTO param) {
        System.out.println("debug: createCrew + " + crewMapper);
        System.out.println("debug: createCrew + " + param);
        crewMapper.createCrewRow(param);
    }

    // 전체 크루 리스트 조회
    public List<CrewResponseDTO> getCrewList() {
        System.out.println("debug: getCrewList + " + crewMapper);
        return crewMapper.selectCrewRow();
    }

    // 특정 크루 리스트 조회 (종목)
    public List<CrewResponseDTO> getCrewSportList(Map<String, String> map) {
        System.out.println("debug: getCrewSportList + " + crewMapper);
        System.out.println("debug: getCrewSportList + " + map);
        return crewMapper.selectCrewSportRow(map);
    }

    // 특정 크루 정보 조회
    public CrewResponseDTO getCrewInfo(Map<String, Integer> map) {
        System.out.println("debug: getCrewInfo + " + crewMapper);
        System.out.println("debug: getCrewInfo + " + map);
        return crewMapper.selectCrewInfoRow(map);
    }

    // 크루원 추가
    public void addCrewMember(CrewMemberRequestDTO param) {
        System.out.println("debug: addCrewMember + " + crewMapper);
        System.out.println("debug: addCrewMember + " + param);
        crewMapper.insertCrewMemberRow(param);
    }

    // --------- 크루 소개 ---------

    // 크루 소개 수정
    public void updateCrewIntro(CrewRequestDTO param) {
        System.out.println("debug: updateCrewIntro + " + crewMapper);
        System.out.println("debug: updateCrewIntro + " + param);
        crewMapper.updateCrewIntroRow(param);
    }

    // 크루 관리 수정
    public void updateCrewInfo(CrewRequestDTO param) {
        System.out.println("debug: updateCrewInfo + " + crewMapper);
        System.out.println("debug: updateCrewInfo + " + param);
        crewMapper.updateCrewInfoRow(param);
    }

    // 크루 삭제
    public void deleteCrew(CrewRequestDTO param) {
        System.out.println("debug: deleteCrew + " + crewMapper);
        System.out.println("debug: deleteCrew + " + param);
        crewMapper.deleteCrewRow(param);
    }

    // --------- 크루원 정보 ---------

    // 크루원 가입 승인
    public void approveCrewMember(CrewMemberRequestDTO param) {
        System.out.println("debug: approveCrewMember + " + crewMapper);
        System.out.println("debug: approveCrewMember + " + param);
        crewMapper.updateCrewMemberRow(param);
    }

    // 크루원 조회
    public List<CrewMemberResponseDTO> getCrewMemberList(Map<String, Integer> map) {
        System.out.println("debug: getCrewMemberList + " + crewMapper);
        System.out.println("debug: getCrewMemberList + " + map);
        return crewMapper.selectCrewMemberRow(map);
    }

    // 크루원 삭제
    public void deleteCrewMember(CrewMemberRequestDTO param) {
        System.out.println("debug: deleteCrewMember + " + crewMapper);
        System.out.println("debug: deleteCrewMember + " + param);
        crewMapper.deleteCrewMemberRow(param);
    }

    // --------- 크루 게시판 ---------

    // 크루 게시물 등록
    public void createCrewPost(CrewPostRequestDTO param) {
        System.out.println("debug: createCrewPost + " + crewMapper);
        System.out.println("debug: createCrewPost + " + param);
        crewMapper.insertCrewPostRow(param);
    }

    // 크루 게시물 전체 조회
    public List<CrewPostResponseDTO> getCrewPostList(Map<String, Integer> map) {
        System.out.println("debug: getCrewPostList + " + crewMapper);
        System.out.println("debug: getCrewPostList + " + map);
        return crewMapper.selectCrewPostListRow(map);
    }

    // 크루 게시물 상단 공지, 일반 고정 3개씩
    public List<CrewPostResponseDTO> getCrewTopPostList(CrewPostRequestDTO param) {
        System.out.println("debug: getCrewTopPostList + " + crewMapper);
        System.out.println("debug: getCrewTopPostList + " + param);
        return crewMapper.selectCrewTopPostRow(param);
    }

    // 크루 게시물 공지/인기/일반 조회
    public List<CrewPostResponseDTO> getCrewNoticePostList(CrewPostRequestDTO param) {
        System.out.println("debug: getCrewNoticePostList + " + crewMapper);
        System.out.println("debug: getCrewNoticePostList + " + param);
        return crewMapper.selectCrewNoticePostRow(param);
    }

    // 크루 게시물 상세 조회
    public CrewPostResponseDTO getCrewPost(CrewPostRequestDTO param) {
        System.out.println("debug: getCrewPost + " + crewMapper);
        System.out.println("debug: getCrewPost + " + param);
        return crewMapper.selectCrewPostRow(param);
    }

    // 크루 게시물 수정
    public void updateCrewPost(CrewPostRequestDTO param) {
        System.out.println("debug: updateCrewPost + " + crewMapper);
        System.out.println("debug: updateCrewPost + " + param);
        crewMapper.updateCrewPostRow(param);
    }

    // --------- 크루 댓글 ---------

    // 크루 댓글 작성
    public void createCrewComment(CrewCommentsRequestDTO param) {
        System.out.println("debug: createCrewComment + " + crewMapper);
        System.out.println("debug: createCrewComment + " + param);
        crewMapper.insertCrewCommentRow(param);
    }

    // 크루 댓글 조회
    public List<CrewCommentsResponseDTO> getCrewCommentList(CrewCommentsRequestDTO param) {
        System.out.println("debug: getCrewCommentList + " + crewMapper);
        System.out.println("debug: getCrewCommentList + " + param);
        return crewMapper.selectCrewCommentsRow(param);
    }

    // 크루 댓글 삭제
    public void deleteCrewComment(CrewCommentsRequestDTO param) {
        System.out.println("debug: deleteCrewComment + " + crewMapper);
        System.out.println("debug: deleteCrewComment + " + param);
        crewMapper.deleteCrewCommentRow(param);
    }
}