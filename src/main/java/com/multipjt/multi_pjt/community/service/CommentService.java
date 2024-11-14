package com.multipjt.multi_pjt.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.badge.dao.UserActivityRecordMapper;

import com.multipjt.multi_pjt.community.dao.CommentMapper;
import com.multipjt.multi_pjt.community.domain.comments.CommentRequestDTO;
import com.multipjt.multi_pjt.community.domain.comments.CommentResponseDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    
    @Autowired
    private CommentMapper commentMapper;


    @Autowired
    private UserActivityRecordMapper userActivityRecordMapper;


    // 댓글 저장
    public Map<String, Object> createComment(CommentRequestDTO params, Map<String,Object> map){
        System.out.println(" service - create comment ");

        Map<String, Object> response = new HashMap<>();
        userActivityRecordMapper.insertActivityAndUpdatePoints(map);
        int result = commentMapper.commentInsert(params);
        System.out.println("result : " + result);

        if(result == 1 ){
            response.put("result", true);
            
       
        }else{
            response.put("result", false);
        }
        
        return response;


    }

    // 댓글 list
    public List<CommentResponseDTO> postDetailComment(Map<String, Object> map){
        System.out.println("service - postDetailComment");


        // 댓글 list 
        List<CommentResponseDTO> list = commentMapper.getPostById(map);

        int member_id = (Integer)map.get("member_id");


        if(list.size() > 0 && list != null){
             
            for (CommentResponseDTO comment : list) {
                // 댓글 작성자 여부 설정
                boolean isCommentAuthor = comment.getMember_id() == member_id;
                comment.setCommentAuthor(isCommentAuthor);
    
                // 각 댓글에 대해 like/dislike 상태 조회 및 설정
                Map<String, Integer> exlike = new HashMap<>();
                exlike.put("comments_id", comment.getComments_id());
                exlike.put("member_id", member_id);
    
                // 좋아요 상태 확인
                boolean likeStatus = commentMapper.commitCommentLike(exlike);
                comment.setComment_like_status(likeStatus);
    
                // 싫어요 상태 확인
                boolean dislikeStatus = commentMapper.commitCommentDisLike(exlike);
                comment.setComment_dislike_status(dislikeStatus);
    
                System.out.println("댓글 작성자 true/false : " + comment.isCommentAuthor());
                System.out.println("좋아요 true/false : " + likeStatus);
                System.out.println("싫어요 true/false : " + dislikeStatus);
            }
            return list;
        
        }else {
            list = new ArrayList<>();
        
            return  list; 

        }
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

    



    // 댓글  좋아요 저장
    public Map<String, Object> commentLikeInsert(Map<String, Integer> map){
        System.out.println(" service - likeInsert  " );
            
            Map<String,Object> response = new HashMap<>();
            int likeCount = 0;
            // 댓글 좋아요 존재 확인
            boolean Ex = commentMapper.commitCommentLike(map);
            try{
                if(Ex){
                        System.out.println("테이블에 이미 있음");
                        response.put("Extable" , true);
                }else if(!Ex){
                    
                        System.out.println("Map : " + map);
    
                        // CommentActivity 테이블에 넣기
                        commentMapper.commentLikeACInsert(map);
    
                        // 댓글 좋아요  수 가져오기        
                        likeCount = commentMapper.commentLikeCount(map);
                   
                        System.out.println("likeCount : " + likeCount);
                        // 댓글  좋아요 수 올리기
                        likeCount += 1;
                        map.put("comment_like_counts", likeCount);

                        // 좋아요 수 업데이트 
                        int result = commentMapper.commentLikeCountUpdate(map);
                         
                        // Controller 넘기기
                        if(result == 1){
                            System.out.println("좋아요 수 업데이트 성공");
                            response.put("result", true);
                            response.put("message", "좋아요 수 업데이트 성공");
                        }else{
                            System.out.println("좋아요 수 업데이트 실패");
                            response.put("result", false);
                        }
                }
                // 게시글에서 좋아요  수 가져오기        
                likeCount = commentMapper.commentLikeCount(map);
                response.put("comment_like_counts", likeCount);
            }catch(Exception e){
                e.printStackTrace();;
                response.put("result", false);
                response.put("message", "오류가 발생했습니다. 다시 시도해 주세요.");
            }
    
    
            System.out.println("service - like insert 탈출 ");
            return response;
    
        }

    // 좋아요 삭제
    public Map<String, Object> commentLikeDelete(Map<String, Integer> map){
        System.out.println(" service - likeDelete ");
        Map<String,Object> response = new HashMap<>();

        boolean likestatus = commentMapper.commitCommentLike(map);
        
            try{
                // 존재 해야 삭제 시작
                if(likestatus){
                    boolean status = false;
                    // 게시글 좋아요 수 가져오기
                    int LikeCount = commentMapper.commentLikeCount(map);    
                    System.out.println("좋아요 수 likeCount: " + LikeCount);
                    
                    // 좋아요 수 -1
                    LikeCount -= 1;
                    map.put("comment_like_counts", LikeCount);
                    int result = commentMapper.commentLikeCountUpdate(map);
                    
                    // Activity 테이블에서 삭제
                    int deleteStatus = commentMapper.commentLikeACDelete(map);
                    System.out.println(" 삭제 상태 값 1 : true, 0 false    : " + deleteStatus);

                    if(deleteStatus == 1 ){
                        System.out.println("삭제 성공 : true(deleteStatus = 1)");
                        // Controller 넘기기
                        if(result == 1){
                            System.out.println("좋아요 수 업데이트 성공");
                            status = true; 
                            response.put("result", status);  
                            response.put("message", "좋아요 수 업데이트 성공");
                        }else{
                            System.out.println("좋아요 수 업데이트 실패");
                            status = false;
                            response.put("result", status);
                            response.put("message", "좋아요 수 업데이트 실패");
                        }

                    }else if(deleteStatus == 0 ){
                        System.out.println("삭제 실패 : false (deleteStatus = 0)");
                        throw new IllegalStateException("삭제 작업이 실패했습니다.");
                    }
                }else if(!likestatus){
                    System.out.println("테이블에 이미 없음");    
                    response.put("Extable" , false);
                    response.put("message", "테이블에 이미 없음");
                   
                }

            }catch(NullPointerException e){
                System.err.println("NullPointerException 발생 : " + e.getMessage());
                e.printStackTrace();
            }catch(IllegalStateException e){

                System.err.println("IllegalStateException 발생: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("예기치 않은 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        response.put("comment_like_counts", commentMapper.commentLikeCount(map));
        return response;

    }







    // 싫어요 저장
    public Map<String, Object> commentDislikeInsert(Map<String, Integer> map){
        System.out.println(" service - unlikeInsert  " );
            
            Map<String,Object> response = new HashMap<>();
            int unLikeCount = 0;
            // 싫어요 존재 확인
            boolean Ex = commentMapper.commitCommentDisLike(map);
            try{
                if(Ex){
                        System.out.println("테이블에 이미 있음");
                        response.put("Extable" , true);
                        
                }else if(!Ex){
                    
                        System.out.println("Map : " + map);
    
                        // UserActivity 테이블에 넣기
                        commentMapper.commentDisLikeACInsert(map);
    
                        // 게시글에서 싫어요  수 가져오기        
                        unLikeCount = commentMapper.commentDisLikeCount(map);
    
                        // 게시글에서 싫어요 수 올리기
                        unLikeCount += 1;
                        map.put("comment_dislike_counts", unLikeCount);
    
                        int result = commentMapper.commentDisLikeCountUpdate(map);
    
                    
                        // Controller 넘기기
                        if(result == 1){
                            System.out.println("싫어요 수 업데이트 성공");
                            response.put("result", true);
                            response.put("message", "싫어요 수 업데이트 성공");
                        }else{
                            System.out.println("싫어요 수 업데이트 실패");
                            response.put("result", false);
                        }
                }
                // 게시글에서 싫어요  수 가져오기        
                unLikeCount = commentMapper.commentDisLikeCount(map);
                response.put("comment_dislike_counts", unLikeCount);
            }catch(Exception e){
                e.printStackTrace();;
                response.put("result", false);
                response.put("message", "오류가 발생했습니다. 다시 시도해 주세요.");
            }
    
    
            System.out.println("service - unlick insert 탈출 ");
            return response;
    
        }
    
    
        // 싫어요 삭제
        public Map<String, Object> commentDislikeDelete(Map<String, Integer> map){
            System.out.println(" service - unlikeDelete ");
            Map<String,Object> response = new HashMap<>();
    
            boolean likestatus = commentMapper.commitCommentDisLike(map);
            
                try{
                
                    if(likestatus){
                        boolean status = false;
                        // 게시글 싫어요 수 가져오기
                        int unLikeCount = commentMapper.commentDisLikeCount(map);    
                        System.out.println("싫어요 수 unlikeCount: " + unLikeCount);
                        
                        // 싫어요 수 -1
                        unLikeCount -= 1;
                        map.put("comment_dislike_counts", unLikeCount);
                        int result = commentMapper.commentDisLikeCountUpdate(map);
                        
                        // Activity 테이블에서 삭제
                        int deleteStatus = commentMapper.commentDisLikeACDelete(map);
                        System.out.println(" 삭제 상태 값 1 : true, 0 false    : " + deleteStatus);
    
                        if(deleteStatus == 1 ){
                            System.out.println("삭제 성공 : true(deleteStatus = 1)");
                            
                            if(result == 1){
                                System.out.println("싫어요 수 업데이트 성공");
                                status = true; 
                                response.put("result", status);  
                                response.put("message", "싫어요 수 업데이트 성공");
                            }else{
                                System.out.println("싫어요 수 업데이트 실패");
                                status = false;
                                response.put("result", status);
                                response.put("message", "싫어요 수 업데이트 실패");
                            }
    
                        }else if(deleteStatus == 0 ){
                            System.out.println("삭제 실패 : false (deleteStatus = 0)");
                            throw new IllegalStateException("삭제 작업이 실패했습니다.");
                        }
                    }else if(!likestatus){
                        System.out.println("테이블에 이미 없음");
                        response.put("Extable" , false);
                        response.put("message", "테이블에 이미 없음");
                       
                    }
    
                }catch(NullPointerException e){
                    System.err.println("NullPointerException 발생 : " + e.getMessage());
                    e.printStackTrace();
                }catch(IllegalStateException e){
    
                System.err.println("IllegalStateException 발생: " + e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    System.err.println("예기치 않은 오류 발생: " + e.getMessage());
                    e.printStackTrace();
                }
            response.put("comment_dislike_counts", commentMapper.commentDisLikeCount(map));
            return response;
    
        }
}
