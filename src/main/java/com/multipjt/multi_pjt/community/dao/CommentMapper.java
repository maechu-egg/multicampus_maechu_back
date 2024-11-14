package com.multipjt.multi_pjt.community.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.community.domain.comments.CommentRequestDTO;
import com.multipjt.multi_pjt.community.domain.comments.CommentResponseDTO;

@Mapper
public interface CommentMapper {

    // 댓글 등록
    public int commentInsert(CommentRequestDTO cdto);
    
     // 게시글 번호로 조회 댓글 조회
    public List<CommentResponseDTO> getPostById( Map<String, Object> map);

    // 댓글 삭제
    public int commentDelete(Map<String, Integer> map);

    // 댓글 삭제 확인
    public CommentResponseDTO getCommentById(int cid);


    // 댓글 존재 확인
    public Boolean commitComment(Map<String, Integer> map);


    // 댓글 개수
    public int countCommentPost(Map<String, Integer> map);


    // 댓글 좋아요 확인
    public boolean commitCommentLike(Map<String, Integer> map);

    // 댓글 좋아요 테이블 insert
    public int commentLikeACInsert(Map<String, Integer> map);
    
    // 댓글 좋아요 테이블 삭제
    public int commentLikeACDelete(Map<String, Integer> map);

    // 댓글 좋아요 수 가져오기
    public int commentLikeCount(Map<String, Integer> map);

    // 댓글 좋아요 +1
    public int commentLikeCountUpdate(Map<String, Integer> map);

    // 댓글 싫어요 확인 
    public boolean commitCommentDisLike(Map<String, Integer> map);

    // 댓글 싫어요 수 조회
    public int commentDisLikeCount(Map<String, Integer> map);

    // 댓글 싫어요 수 변경
    public int commentDisLikeCountUpdate(Map<String, Integer> map);

    // 댓글 싫어요 테이블에 넣기
    public int commentDisLikeACInsert(Map<String, Integer> map);

    // 댓글 싫어요 테이블에서 삭제
    public int commentDisLikeACDelete(Map<String, Integer> map);
    

} 
