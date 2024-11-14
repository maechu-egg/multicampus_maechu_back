package com.multipjt.multi_pjt.community.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.multipjt.multi_pjt.community.service.UserActivityService;
import com.multipjt.multi_pjt.jwt.JwtTokenProvider;

import java.util.Map;
import java.util.HashMap;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/useractivity")
public class UserActivityController {
    
    @Autowired
    private UserActivityService userActivityService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    // 좋아요 등록
    @PostMapping("{postId}/likeinsert")
    public ResponseEntity<Map<String, Object>> likeInsert(@PathVariable("postId") int postId, 
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

           System.out.println("Controller client endpoint : /useractivity/likeinsert ");
           System.out.println("postId : " + postId);
           ResponseEntity<Map<String, Object>> response = null;
           Map<String, Object> result = new HashMap<>();
      
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
            System.out.println("userId : " + userId);
           try{    
            
               
               Map<String, Integer> map = new HashMap<>();
               map.put("post_id", postId);
               map.put("member_id", userId);

               
               result = userActivityService.likeInsert(map);
           
               System.out.println("controller - service에서 넘어온 result : " + result);
               if(result.get("result") != null && (boolean)result.get("result")){
                   result.put("message", "좋아요 추가 성공");
                   response = ResponseEntity.ok(result);
               }else if(result.get("Extable") != null && (boolean)result.get("Extable")){
                   result.put("message", "테이블에 이미 있습니다.");
                   result.put("liked", true);
                   response =  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
               }else {
                   result.put("message", "좋아요 추가 실패");
                   response =  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
               }
          
            }catch(Exception e){
                System.err.println("오류 발생: " + e.getMessage());
                e.printStackTrace();
                result.put("message", "서버에서 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
                response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            }
        }else{
            result.put("message", "로그인이 필요합니다.");
            response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
            return response;
    }


    // 좋아요 삭제
    @DeleteMapping("/{postId}/likedelete")
    public ResponseEntity<Map<String, Object>> likeDelete(@PathVariable("postId") int postId, 
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ){
       ResponseEntity<Map<String, Object>> response = null;
       Map<String, Object> result = new HashMap<>(); 
     
           if (authHeader != null && authHeader.startsWith("Bearer ")) {
               String token = authHeader.substring(7); // "Bearer " 접두사 제거
               int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
               System.out.println("userId : " + userId);
               try{
               System.out.println("Controller clien endpoint : /useractivity/likedelete");
               System.out.println("postId :" + postId);
               System.out.println("member_id :" + userId);
   
               Map<String, Integer > map = new HashMap<>();
               map.put("post_id", postId);
               map.put("member_id", userId);
               
            
               result = userActivityService.likeDelete(map);
               System.out.println("controller - " + result);
               System.out.println("get Result - " + result.get("result"));
               System.out.println("get Extable - "  + result.get("Extable")  );
               if(result.get("result") != null && (boolean)result.get("result")  ) {
               
                   result.put("message", "좋아요 삭제 성공");
                   response = ResponseEntity.ok(result);
               }else if(result.get("Extable") != null && !(boolean)result.get("Extable") ){
                   result.put("message", "테이블에 없습니다.");
                   response =  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
               }else {
                   result.put("message", "좋아요 삭제 실패");
                   response =  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
               }
            } catch(Exception e){
                System.err.println("오류 발생: " + e.getMessage());
                e.printStackTrace();
                result.put("message", "서버에서 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
                response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            } 

           }else{
               result.put("message", "로그인이 필요합니다.");
               response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
           }
        
          return response;                                  
    }


    
     // 싫어요 등록
     @PostMapping("{postId}/unlikeinsert")
     public ResponseEntity<Map<String, Object>> unlikeInsert(@PathVariable("postId") int postId, 
                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

            System.out.println("Controller client endpoint : /useractivity/unlikeinsert ");
            System.out.println("postId : " + postId);
            ResponseEntity<Map<String, Object>> response = null;
            Map<String, Object> result = new HashMap<>();
           
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
                System.out.println("userId : " + userId);
            
                try{  
                    Map<String, Integer> map = new HashMap<>();
                    map.put("post_id", postId);
                    map.put("member_id", userId);

                    
                    result = userActivityService.unLikeInsert(map);
                    
                    System.out.println("controller - service에서 넘어온 result : " + result);
                    if(result.get("result") != null && (boolean)result.get("result")){
                        result.put("message", "싫어요 추가 성공");
                        response = ResponseEntity.ok(result);
                    }else if(result.get("Extable") != null && (boolean)result.get("Extable")){
                        result.put("message", "테이블에 이미 있습니다.");
                        response =  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
                    }else {
                        result.put("message", "싫어요 추가 실패");
                        response =  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
                    }
                }catch(Exception e){
                    System.err.println("오류 발생: " + e.getMessage());
                    e.printStackTrace();
                    result.put("message", "서버에서 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
                    response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
                }
            }else{
                result.put("message", "로그인이 필요합니다.");
                response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }
       
        return response;
         
     }
     
 
 
     // 싫어요 삭제
     @DeleteMapping("/{postId}/unlikedelete")
     public ResponseEntity<Map<String, Object>> unlikeDelete(@PathVariable("postId") int postId, 
                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
     ){
        ResponseEntity<Map<String, Object>> response = null;
        Map<String, Object> result = new HashMap<>(); 
       
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
                System.out.println("userId : " + userId);
                try{
                System.out.println("Controller clien endpoint : /useractivity/unlikedelete");
                System.out.println("postId :" + postId);
                System.out.println("member_id :" + userId);
    
                Map<String, Integer > map = new HashMap<>();
                map.put("post_id", postId);
                map.put("member_id", userId);
                
                result = userActivityService.unLikeDelete(map);
                System.out.println("controller - " + result);
                System.out.println("get Result - " + result.get("result"));
                System.out.println("get Extable - "  + result.get("Extable")  );
                if(result.get("result") != null && (boolean)result.get("result")  ) {
                
                    result.put("message", "싫어요 삭제 성공");
                    response = ResponseEntity.ok(result);
                }else if(result.get("Extable") != null && !(boolean)result.get("Extable") ){
                    result.put("message", "테이블에 없습니다.");
                    response =  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
                }else {
                    result.put("message", "싫어요 삭제 실패");
                    response =  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
                }
            } catch(Exception e){
                System.err.println("오류 발생: " + e.getMessage());
                e.printStackTrace();
                result.put("message", "서버에서 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
                response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            } 
            }else{
                result.put("message", "로그인이 필요합니다.");
                response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }
          
           return response;                                  
     }
}
