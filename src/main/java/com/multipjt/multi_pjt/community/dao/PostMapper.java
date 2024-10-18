package com.multipjt.multi_pjt.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.multipjt.multi_pjt.community.domain.posts.PostRequestDTO;
import com.multipjt.multi_pjt.community.domain.posts.PostResponseDTO;



@Mapper
public interface PostMapper {
    
    public int postInsert(PostRequestDTO pdto);

     //  member_id로 게시글 조회
    List<PostResponseDTO> getMemberById(@Param("member_id") int mid);

    
    public int postUpdate(PostResponseDTO pdto);


    PostResponseDTO getPostById(@Param("post_id") int pid);

    List<PostResponseDTO> postSelectSport(@Param("post_sport") String psport);
    


}
