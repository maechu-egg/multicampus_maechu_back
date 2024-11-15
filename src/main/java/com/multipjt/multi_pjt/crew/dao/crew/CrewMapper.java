package com.multipjt.multi_pjt.crew.dao.crew;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import com.multipjt.multi_pjt.crew.domain.crew.CrewCommentsRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewCommentsResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostLikeRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostLikeResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewResponseDTO;

@Mapper
public interface CrewMapper {
    // --------- 크루 찾기 ---------

    // 크루 생성
    public void createCrewRow(CrewRequestDTO param);

    // 추천 크루 리스트 조회
    public List<CrewResponseDTO> selectCrewRow(int member_id);
    
    // 도 단위 일치 크루 리스트 조회
    public List<CrewResponseDTO> selectCrewByRegionRow(int member_id);

    // 추천 크루 리스트 조회 limit 3
    public List<CrewResponseDTO> selectCrewForHomepageRow(int member_id);

    // 도 단위 일치 크루 리스트 조회 limit 4
    public List<CrewResponseDTO> selectCrewByRegionRowForHomepage(int member_id);

    // 특정 크루 정보 조회
    public CrewResponseDTO selectCrewInfoRow(Integer crew_id);

    // 크루원 추가
    public void insertCrewMemberRow(CrewMemberRequestDTO param);

    // 내가 속한 크루 조회
    public List<CrewResponseDTO> selectMyCrewRow(Integer member_id);

    // --------- 크루 소개 ---------

    // 크루 소개 수정
    public void updateCrewIntroRow(CrewRequestDTO param);

    // 크루장 조회
    public Integer selectCrewLeaderIdRow(Integer crew_id);

    // 크루 관리 수정
    public void updateCrewInfoRow(CrewRequestDTO param);

    // 크루 삭제
    public void deleteCrewRow(Integer crew_id);

    // --------- 크루원 정보 ---------
    
    // 크루원 가입 승인
    public void updateCrewMemberRow(CrewMemberRequestDTO param);

    // 크루원 조회
    public List<CrewMemberResponseDTO> selectCrewMemberRow(int crew_id);

    // 크루원 삭제
    public void deleteCrewMemberRow(CrewMemberRequestDTO param);

    // --------- 크루 게시판 ---------
    
    // 크루 게시물 등록
    public void insertCrewPostRow(CrewPostRequestDTO param);

    // 크루 게시판 게시물 전체 조회 (페이지네이션 적용)
    public List<CrewPostResponseDTO> selectCrewPostListRow(Map<String, Object> params);

    // 크루 게시판 게시물 전체 조회 개수
    public int selectCrewPostListCountRow(int crew_id);
    
    // 크루 게시판 상단 공지, 인기 고정 3개씩 조회
    public List<CrewPostResponseDTO> selectCrewTopPostRow(CrewPostRequestDTO param);

    // 크루 게시판 공지/인기/일반 게시물 조회
    public List<CrewPostResponseDTO> selectCrewNoticePostRow(CrewPostRequestDTO param);

    // 크루 게시물 상세 조회
    public CrewPostResponseDTO selectCrewPostRow(CrewPostRequestDTO param);

    // 크루 게시물 수정
    public void updateCrewPostRow(CrewPostRequestDTO param);

    // 크루 게시물 삭제
    public void deleteCrewPostRow(CrewPostRequestDTO param);

    // --------- 크루 게시물 좋아요 ---------

    // 크루 게시물 좋아요 상태 확인
    public List<CrewPostLikeResponseDTO> selectCrewPostLikeRow(Map<String, Object> params);
    
    // 크루 게시물 좋아요 수 증가
    public void increasePostLikeRow(int crew_post_id);

    // 크루 게시물 좋아요 수 감소
    public void decreasePostLikeRow(int crew_post_id);

    // 크루원 게시물 별 좋아요 기록 추가
    public void insertCrewPostLikeRow(CrewPostLikeRequestDTO param);

    // 크루원 게시물 별 좋아요 기록 삭제
    public void deleteCrewPostLikeRow(CrewPostLikeRequestDTO param);

    // 일반 게시물 조회
    public List<CrewPostResponseDTO> selectPopularPostRow();

    // 크루 게시물 상태 인기 변경
    public void updatePostStatusRow(int crew_post_id);

    // 크루 멤버수 조회
    public int selectCrewMemberCountRow(int crew_id);

    // 크루 게시물 좋아요수 조회
    public int selectPostLikeCountRow(int crew_post_id);

    // --------- 크루 댓글 ---------
    
    // 크루 댓글 작성
    public void insertCrewCommentRow(CrewCommentsRequestDTO param);

    // 크루 게시판 게시물 댓글 조회
    public List<CrewCommentsResponseDTO> selectCrewCommentsRow(int crew_post_id);

    // 크루 게시판 게시물 댓글 삭제
    public void deleteCrewCommentRow(CrewCommentsRequestDTO param);
}
