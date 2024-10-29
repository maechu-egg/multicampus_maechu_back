package com.multipjt.multi_pjt.community.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.community.domain.UserActivity.UserActivityRequestDTO;
import com.multipjt.multi_pjt.community.service.UserActivityService;
import java.util.Map;
import java.util.HashMap;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/useractivity")
public class UserActivityController {
    
    @Autowired
    private UserActivityService userActivityService;

    // 좋아요 등록
    @PostMapping("{postId}/likeinsert")
    public ResponseEntity<String> likeInsert(@PathVariable("postId") int postId, 
                                            @RequestBody UserActivityRequestDTO request) {
        System.out.println("Controller client endpoint : /useractivity/likeinsert ");
        System.out.println("postId : " + postId);
        System.out.println("userId : " + request.getMember_id());
        Map<String, Integer> map = new HashMap<>();
        map.put("post_id", postId);
        map.put("member_id", request.getMember_id());
        boolean result = userActivityService.likeInsert(map);

        if(result){
            return ResponseEntity.ok("좋아요 추가 성공");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("좋아요 추가 실패");
        }
        
        
    }
    


    // 좋아요 삭제
    @DeleteMapping("/{postId}/likedelete")
    public ResponseEntity<String> likeDelete(@PathVariable("postId") int postId, 
                                            @RequestBody UserActivityRequestDTO request
    ){
        System.out.println("Controller clien endpoint : /useractivity/likedelete");
        System.out.println("postId :" + postId);
        System.out.println("member_id :" + request.getMember_id());

        Map<String, Integer > map = new HashMap<>();
        map.put("post_id", postId);
        map.put("member_id", request.getMember_id());

        boolean result = userActivityService.likeDelete(map);

        if(result ){
            return ResponseEntity.ok("좋아요 삭제 성공");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("좋아요 삭제 실패");
        }
                                            
    }




}
