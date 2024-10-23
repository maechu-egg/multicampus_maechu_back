package com.multipjt.multi_pjt.community.ctrl;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.community.domain.posts.PostResponseDTO;
import com.multipjt.multi_pjt.community.service.PostService;



@RestController
@RequestMapping("/Posts")
public class PostController {


    @Autowired
    private PostService postservice;


   
    
 


  

    
}
