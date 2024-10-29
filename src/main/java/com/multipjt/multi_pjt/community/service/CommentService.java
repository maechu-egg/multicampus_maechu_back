package com.multipjt.multi_pjt.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.community.dao.CommentMapper;
import com.multipjt.multi_pjt.community.dao.PostMapper;
import com.multipjt.multi_pjt.community.domain.comments.CommentRequestDTO;
import com.multipjt.multi_pjt.community.domain.comments.CommentResponseDTO;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostMapper postMapper;


    // 댓글 저장
    public void createComment(CommentRequestDTO params){
        System.out.println(" service - create comment ");

        commentMapper.commentInsert(params);
    }

    // 댓글 list
    public List<CommentResponseDTO> postDetailComment(Map<String, Integer> map){
        System.out.println("service - postDetailComment");

        List<CommentResponseDTO> list = postMapper.postDetailComment(map);

        return list;
    }

    // 댓글 삭제
    public void commentDelete(Map<String, Integer> map){
        System.out.println(" service - commentDelete");

        int result =  commentMapper.commentDelete(map);

        System.out.println(" 댓글이 지워지면 result = 1  - " + result);
    }

    



}
