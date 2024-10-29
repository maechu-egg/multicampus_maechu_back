package com.multipjt.multi_pjt.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.community.dao.UserActivityMapper;



import java.util.Map;

@Service
public class UserActivityService {
    
    @Autowired
    private UserActivityMapper userActivityMapper;

    // 좋아요 저장
    public boolean likeInsert(Map<String, Integer> map){
        System.out.println(" service - likeInsert  " );
        // 좋아요 존재 확인
        if(userActivityMapper.commitLike(map)){
            System.out.println("테이블에 이미 있음");
            return false;
        }
        System.out.println("Map : " + map);

        // UserActivity 테이블에 넣기
        userActivityMapper.likeACInsert(map);

        // 게시글에서 좋아요  수 가져오기        
        int likeCount = userActivityMapper.likeCount(map);

        // 게시글에서 좋아요 수 올리기
        likeCount += 1;
        map.put("post_like_counts", likeCount);
        int result = userActivityMapper.postLikeCountUpdate(map);

        // Controller 넘기기
        if(result == 1){
            System.out.println("좋아요 수 업데이트 성공");
            return true;
        }else{
            System.out.println("좋아요 수 업데이트 실패");
            return false;
        }

    }

    // 좋아요 삭제
    public boolean likeDelete(Map<String, Integer> map){
        System.out.println(" service - likeDelete ");
        
        // 테이블 상태 확인
        boolean likestatus= userActivityMapper.commitLike(map);
        System.out.println("존재 하면 1 아니면 0" + likestatus);

        boolean status = false;
        if(likestatus){
                      
            // 게시글 좋아요 수 가져오기
            int likeCount = userActivityMapper.likeCount(map);    
            System.out.println("좋아요 수 likeCount: " + likeCount);
            
            // 좋아요 수 -1
            likeCount -= 1;
            map.put("post_like_counts", likeCount);
            int result = userActivityMapper.postLikeCountUpdate(map);
            
            // Activity 테이블에서 삭제
            int deleteStatus = userActivityMapper.likeACDelete(map);
            System.out.println(" 삭제 상태 값 1 : true, 0 false    : " + deleteStatus);

            // Controller 넘기기
            if(result == 1){
                System.out.println("좋아요 수 업데이트 성공");
                status = true;   
            }else{
                System.out.println("좋아요 수 업데이트 실패");
                status = false;
            }
        }

        return status;

    }

    // 조회수 +1
    public void viewCount(Map<String, Integer> map){
        System.out.println("service - viewCount : 조회수 올리기");
        // post 의 view 수 가져오기 
        int viewCount = userActivityMapper.viewCount(map);
        System.out.println(viewCount);
    
        // +1  update
        viewCount += 1;
        map.put("post_views" , viewCount);
        int viewUpdate = userActivityMapper.postViewCountAdd(map);
        System.out.println("정상적으로 업데이트면 1 - " + viewUpdate);
        
    }

    // UserActivity 테이블에 넣기
    public void viewInsert(Map<String, Integer> map){
        System.out.println( "service viewInsert : UserActivity");
        
        userActivityMapper.postViewInsert(map);
    } 

}
