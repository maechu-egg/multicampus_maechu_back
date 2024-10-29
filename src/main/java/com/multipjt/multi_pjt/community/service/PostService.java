package com.multipjt.multi_pjt.community.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.community.dao.PostMapper;
import com.multipjt.multi_pjt.community.dao.UserActivityMapper;
import com.multipjt.multi_pjt.community.domain.comments.CommentResponseDTO;
import com.multipjt.multi_pjt.community.domain.posts.PostRequestDTO;
import com.multipjt.multi_pjt.community.domain.posts.PostResponseDTO;


@Service
public class PostService {
    
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserActivityMapper userActivityMapper;

    // 전체 페이지 
    public List<PostResponseDTO> getAllPagePosts(int page, int size){
        System.out.println("service : getAllPagePosts 조회");
       
        int offset = (page - 1 ) * size ;
        
        List<PostResponseDTO>  list = postMapper.postAllSelect(size, offset);
        System.out.println("service   list - " + list );
        return list;
    }


    // 전체 페이지 수
    public int countPosts(){
        System.out.println("service : countPosts 조회");
        int total = postMapper.countPosts();

        return total;
    }

    // 검색 기록 
    public List<PostResponseDTO> postSelectTCH(Map<String, Object> map){
        System.out.println("service : post_sports 조회");
        int page = (int)map.get("page");
        int size = (int)map.get("size");
        System.out.println("page : " + page);
        System.out.println("size : " + size);
        System.out.println("keywor : " + map.get("keyword") );
        int offset = (page - 1) * size;
        map.put("offset", offset);

        List<PostResponseDTO> searchList = postMapper.postSelectTCH(map);

        return searchList;
    }


    // 운동 페이지 조회 
    public List<PostResponseDTO> postSelectSport(Map<String, Object> map){
        System.out.println("service : post_sports 조회");
        int page = (int)map.get("page");
        int size = (int)map.get("size");
        System.out.println("page : " + page);
        System.out.println("size : " + size);

        int offset = (page - 1) * size;
        map.put("offset", offset);
        List<PostResponseDTO> sportPostList = postMapper.postSelectSport(map);

        return sportPostList;
    }

    // 게시글 등록
    public int postInsert(PostRequestDTO pdto){
        System.out.println("service - postInsert");
        int result = postMapper.postInsert(pdto);

        if(result == 1){
            System.out.println("service : 게시글이 성공적으로 등록되었습니다.");
        }else{
            System.out.println("service : 0 : 게시글 등록에 실패했습니다. ");
        }

        return result;
    }
    

    // 게시글 수정
    public int postUpdate(PostRequestDTO pdto){

        System.out.println("service : postUpdate");
        return postMapper.postUpdate(pdto);
    }

    // 상세 페이지 조회 
    public PostResponseDTO postDetail(Map<String, Integer> map){
        System.out.println(" service postDetail - "   );
        System.out.println("map : " + map);
        // 게시글 정보
        PostResponseDTO result = postMapper.postDetailSelect(map);
        // 댓글 정보
        List<CommentResponseDTO> list = postMapper.postDetailComment(map);
        
        result.setComments(list);
        
        return result;
    }

    // 상세 페이지 - 좋아요 상태
    public boolean postDetailLike(Map<String, Integer> map){
        // 좋아요 정보
        boolean like = userActivityMapper.commitLike(map);
        System.out.println("좋아요 상태 1. true있다 / 없다. false " + like);

        return like;

    }

    // new 회원 / 일주일 이상 활동이 없는 회원인지 판단 
    public int isNewMember(int member_id){

        return postMapper.isNewMember(member_id);
    }

    // new 회원 / 일주일 이상 활동이 없는 회원 추천 리스트
    public List<PostResponseDTO> newMemberRCPost(int member_id){
        return postMapper.newMemberRCPost(member_id);
    }

    // 활동 중인 회원 추천
    public List<PostResponseDTO> exMemberData(int member_id){
        System.out.println("service - exMemberData : member_id : " + member_id);
        // 운동 종목, 키워드  추출 하기
        List<PostResponseDTO> searchKey = postMapper.exMemberData(member_id);
        System.err.println("list size" + searchKey.size());
          
        List<PostResponseDTO> recommendPost = new ArrayList<>();
        if(searchKey.size() > 0){
            recommendPost.addAll(postMapper.findRCPosts(
                searchKey.get(0).getPost_sport(), searchKey.get(0).getPost_sports_keyword(), 3 ));
        }
        if(searchKey.size() > 1){
            recommendPost.addAll(postMapper.findRCPosts(
                searchKey.get(1).getPost_sport(), searchKey.get(1).getPost_sports_keyword(), 1 ));
        }
        if(searchKey.size() > 2){
            recommendPost.addAll(postMapper.findRCPosts(
                searchKey.get(2).getPost_sport(), searchKey.get(2).getPost_sports_keyword(), 1 ));
        }
        
        return recommendPost;
    }

    // 비회원 추천 
    public List<PostResponseDTO> nonMemberRCPost(){

        return postMapper.nonMemberRCPost();
    }

}
