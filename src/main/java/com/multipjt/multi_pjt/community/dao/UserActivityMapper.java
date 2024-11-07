package com.multipjt.multi_pjt.community.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

import com.multipjt.multi_pjt.community.domain.UserActivity.UserActivityRequestDTO;


@Mapper
public interface UserActivityMapper {
    

    // 좋아요 insert commit 
    boolean commitLike(Map<String, Integer> map);

    // 테이블에 좋아요 추가
    int likeACInsert(Map<String, Integer> map);

     // 테이블에 좋아요 삭제
    int likeACDelete(Map<String, Integer> map);

    // posts에서 좋아요 수 가져오기
    int likeCount(Map<String, Integer> map);
    
    // 좋아요 클릭시 좋아요 수 +1 
    int postLikeCountUpdate(Map<String, Integer> map);
    
    // 좋아요 클릭시 좋아요 수 -1 
    int postLikeCountMinus(Map<String, Integer> map);
    
    // view 클릭시 Insert
    int postViewInsert(Map<String, Integer> map);

    // view insert commit
    boolean commitView(UserActivityRequestDTO uadto);
    
    // posts에서 view 수 가져오기
    int viewCount(Map<String, Integer> map);
        
    // view 클릭시 좋아요 수 +1 
    int postViewCountAdd(Map<String, Integer> map);

    // 싫어요 존재 
    boolean commitUnLike(Map<String, Integer> map);

    // 테이블에 좋아요 추가
    int unLikeACInsert(Map<String, Integer> map);

    // 테이블에 좋아요 삭제
    int unLikeACDelete(Map<String, Integer> map);

    // posts에서 싫어요 수 가져오기
    int unLikeCount(Map<String, Integer> map);
    
    // 싫어요 클릭시 싫어요 수 업데이트
    int postUnLikeCountUpdate(Map<String, Integer> map);

 
    
}
