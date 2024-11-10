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
    
} 
