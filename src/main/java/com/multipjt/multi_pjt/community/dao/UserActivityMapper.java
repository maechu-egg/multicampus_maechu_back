package com.multipjt.multi_pjt.community.dao;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.community.domain.UserActivity.UserActivityRequestDTO;
import com.multipjt.multi_pjt.community.domain.posts.PostRequestDTO;


@Mapper
public interface UserActivityMapper {
    
    
    

    // 좋아요 insert commit 
    boolean commitLike(UserActivityRequestDTO uadto);

    // 테이블에 좋아요 추가
    int likeACInsert(UserActivityRequestDTO uadto);
    

     // 테이블에 좋아요 삭제
     int likeACDelete(UserActivityRequestDTO uadto);

    // posts에서 좋아요 수 가져오기
    int likeCount(int post_id);
    
    // 좋아요 클릭시 좋아요 수 +1 
    int postLikeCountAdd(PostRequestDTO pdto);
    
    // 좋아요 클릭시 좋아요 수 -1 
    int postLikeCountMinus(PostRequestDTO pdto);

    
    // view 클릭시 Insert
    int postViewInsert(UserActivityRequestDTO uadto);

    // view insert commit
    boolean commitView(UserActivityRequestDTO uadto);
    

    // posts에서 좋아요 수 가져오기
    int viewCount(int post_id);
        
    // 좋아요 클릭시 좋아요 수 +1 
    int postViewCountAdd(PostRequestDTO pdto);


}
