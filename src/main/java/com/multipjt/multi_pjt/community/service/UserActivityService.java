package com.multipjt.multi_pjt.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.community.dao.UserActivityMapper;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserActivityService {
    
    @Autowired
    private UserActivityMapper userActivityMapper;

    // 좋아요 저장
    public Map<String, Object> likeInsert(Map<String, Integer> map){
        System.out.println(" service - likeInsert  " );
            
            Map<String,Object> response = new HashMap<>();
            int likeCount = 0;
            // 좋아요 존재 확인
            boolean Ex = userActivityMapper.commitLike(map);
            try{
                if(Ex){
                        System.out.println("테이블에 이미 있음");
                        response.put("Extable" , true);
                }else if(!Ex){
                    
                        System.out.println("Map : " + map);
    
                        // UserActivity 테이블에 넣기
                        userActivityMapper.likeACInsert(map);
    
                        // 게시글에서 좋아요  수 가져오기        
                        likeCount = userActivityMapper.likeCount(map);
                   
                        System.out.println("likeCount : " + likeCount);
                        // 게시글에서 좋아요 수 올리기
                        likeCount += 1;
                        map.put("post_like_counts", likeCount);

                        // 좋아요 수 업데이트 
                        int result = userActivityMapper.postLikeCountUpdate(map);
                         
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
                likeCount = userActivityMapper.likeCount(map);
                response.put("likeCount", likeCount);
            }catch(Exception e){
                e.printStackTrace();;
                response.put("result", false);
                response.put("message", "오류가 발생했습니다. 다시 시도해 주세요.");
            }
    
    
            System.out.println("service - like insert 탈출 ");
            return response;
    
        }

    // 좋아요 삭제
    public Map<String, Object> likeDelete(Map<String, Integer> map){
        System.out.println(" service - likeDelete ");
        Map<String,Object> response = new HashMap<>();

        boolean likestatus = userActivityMapper.commitLike(map);
        
            try{
                // 존재 해야 삭제 시작
                if(likestatus){
                    boolean status = false;
                    // 게시글 좋아요 수 가져오기
                    int LikeCount = userActivityMapper.likeCount(map);    
                    System.out.println("좋아요 수 likeCount: " + LikeCount);
                    
                    // 좋아요 수 -1
                    LikeCount -= 1;
                    map.put("post_like_counts", LikeCount);
                    int result = userActivityMapper.postLikeCountUpdate(map);
                    
                    // Activity 테이블에서 삭제
                    int deleteStatus = userActivityMapper.likeACDelete(map);
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
        response.put("likeCount", userActivityMapper.likeCount(map));
        return response;

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





    // 싫어요 저장
    public Map<String, Object> unLikeInsert(Map<String, Integer> map){
    System.out.println(" service - unlikeInsert  " );
        
        Map<String,Object> response = new HashMap<>();
        int unLikeCount = 0;
        // 싫어요 존재 확인
        boolean Ex = userActivityMapper.commitUnLike(map);
        try{
            if(Ex){
                    System.out.println("테이블에 이미 있음");
                    response.put("Extable" , true);
                    
            }else if(!Ex){
                
                    System.out.println("Map : " + map);

                    // UserActivity 테이블에 넣기
                    userActivityMapper.unLikeACInsert(map);

                    // 게시글에서 싫어요  수 가져오기        
                    unLikeCount = userActivityMapper.unLikeCount(map);

                    // 게시글에서 싫어요 수 올리기
                    unLikeCount += 1;
                    map.put("post_unlike_counts", unLikeCount);

                    int result = userActivityMapper.postUnLikeCountUpdate(map);

                
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
            unLikeCount = userActivityMapper.unLikeCount(map);
            response.put("unLikeCount", unLikeCount);
        }catch(Exception e){
            e.printStackTrace();;
            response.put("result", false);
            response.put("message", "오류가 발생했습니다. 다시 시도해 주세요.");
        }


        System.out.println("service - unlick insert 탈출 ");
        return response;

    }


    // 싫어요 삭제
    public Map<String, Object> unLikeDelete(Map<String, Integer> map){
        System.out.println(" service - unlikeDelete ");
        Map<String,Object> response = new HashMap<>();

        boolean likestatus = userActivityMapper.commitUnLike(map);
        
            try{
            
                if(likestatus){
                    boolean status = false;
                    // 게시글 싫어요 수 가져오기
                    int unLikeCount = userActivityMapper.unLikeCount(map);    
                    System.out.println("싫어요 수 unlikeCount: " + unLikeCount);
                    
                    // 싫어요 수 -1
                    unLikeCount -= 1;
                    map.put("post_unlike_counts", unLikeCount);
                    int result = userActivityMapper.postUnLikeCountUpdate(map);
                    
                    // Activity 테이블에서 삭제
                    int deleteStatus = userActivityMapper.unLikeACDelete(map);
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
        response.put("unLikeCount", userActivityMapper.unLikeCount(map));
        return response;

    }

    

}
