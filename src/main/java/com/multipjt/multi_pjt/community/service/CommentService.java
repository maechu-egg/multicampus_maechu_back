package com.multipjt.multi_pjt.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.badge.dao.UserActivityRecordMapper;

import com.multipjt.multi_pjt.community.dao.CommentMapper;
import com.multipjt.multi_pjt.community.dao.PostMapper;
import com.multipjt.multi_pjt.community.domain.comments.CommentRequestDTO;
import com.multipjt.multi_pjt.community.domain.comments.CommentResponseDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserActivityRecordMapper userActivityRecordMapper;


    // 댓글 저장
    public Map<String, Object> createComment(CommentRequestDTO params, Map<String,Object> map){
        System.out.println(" service - create comment ");

        Map<String, Object> response = new HashMap<>();
        
        int result = commentMapper.commentInsert(params);
        System.out.println("result : " + result);

        if(result == 1 ){
            response.put("result", true);
            userActivityRecordMapper.insertActivityAndUpdatePoints(map);
       
        }else{
            response.put("result", false);
        }
        
        return response;


    }

    // 댓글 list
    public List<CommentResponseDTO> postDetailComment(Map<String, Object> map){
        System.out.println("service - postDetailComment");

        List<CommentResponseDTO> list = postMapper.postDetailComment(map);

        return list;
    }

    // 댓글 삭제
    public Map<String, Object> commentDelete(Map<String, Integer> map){
        System.out.println(" service - commentDelete");
        
        Map<String, Object> response = new HashMap<>();
        try{
            boolean Ex = commentMapper.commitComment(map);
            if(Ex){
                int result =  commentMapper.commentDelete(map);
                System.out.println(" 댓글이 지워지면 result = 1  - " + result);
                if(result == 1){
                    response.put("result", true);
                    response.put("message", "댓글 삭제 성공");
                }else{
                    response.put("result", false);
                    response.put("message", "댓글 삭제 실패");
                }
            }else if(!Ex){
                response.put("Extagle", false);
                response.put("message", "댓글이 없습니다.");
            }
        }catch(Exception e){
            e.printStackTrace();

        }
 
        
        response.put("CommentCount", commentMapper.countCommentPost(map));
        
        return response;
  
    }

    



}
