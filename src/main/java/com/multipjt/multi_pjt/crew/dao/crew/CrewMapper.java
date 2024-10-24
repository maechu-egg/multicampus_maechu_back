package com.multipjt.multi_pjt.crew.dao.crew;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

import com.multipjt.multi_pjt.crew.domain.crew.CrewCommentsRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewCommentsResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewResponseDTO;

@Mapper
public interface CrewMapper {
    // --------- 크루 찾기 ---------

    // 크루 생성
    public void createCrewRow(CrewRequestDTO param);

    // 전체 크루 리스트 조회
    public List<CrewResponseDTO> selectCrewRow();

    // 특정 크루 리스트 조회 (종목)
    public List<CrewResponseDTO> selectCrewSportRow(Map<String, String> map);

    // 특정 크루 정보 조회
    public CrewResponseDTO selectCrewInfoRow(Integer crew_id);

    // 크루원 추가
    public void insertCrewMemberRow(CrewMemberRequestDTO param);

    // --------- 크루 소개 ---------

    // 크루 소개 수정
    public void updateCrewIntroRow(CrewRequestDTO pamram);

    // 크루 관리 수정
    public void updateCrewInfoRow(CrewRequestDTO param);

    // 크루 삭제
    public void deleteCrewRow(CrewRequestDTO param);

    // --------- 크루원 정보 ---------
    
    // 크루원 가입 승인
    public void updateCrewMemberRow(CrewMemberRequestDTO param);

    // 크루원 조회
    public List<CrewMemberResponseDTO> selectCrewMemberRow(Map<String, Integer> map);

    // 크루원 삭제
    public void deleteCrewMemberRow(CrewMemberRequestDTO param);

    // --------- 크루 게시판 ---------
    
    // 크루 게시물 등록
    public void insertCrewPostRow(CrewPostRequestDTO param);

    // 크루 게시판 게시물 전체 조회
    public List<CrewPostResponseDTO> selectCrewPostListRow(Map<String, Integer> map);
    
    // 크루 게시판 상단 공지, 일반 고정 3개씩
    public List<CrewPostResponseDTO> selectCrewTopPostRow(CrewPostRequestDTO param);

    // 크루 게시판 공지/인기/일반 게시물 조회
    public List<CrewPostResponseDTO> selectCrewNoticePostRow(CrewPostRequestDTO param);

    // 크루 게시물 상세 조회
    public CrewPostResponseDTO selectCrewPostRow(CrewPostRequestDTO param);

    // 크루 게시물 수정
    public void updateCrewPostRow(CrewPostRequestDTO param);

    // --------- 크루 댓글 ---------
    
    // 크루 댓글 작성
    public void insertCrewCommentRow(CrewCommentsRequestDTO param);

    // 크루 게시판 게시물 댓글 조회
    public List<CrewCommentsResponseDTO> selectCrewCommentsRow(CrewCommentsRequestDTO param);

    // 크루 게시판 게시물 댓글 삭제
    public void deleteCrewCommentRow(CrewCommentsRequestDTO param);
}
