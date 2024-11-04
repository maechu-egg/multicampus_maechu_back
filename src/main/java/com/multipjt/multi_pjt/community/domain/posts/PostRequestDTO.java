package com.multipjt.multi_pjt.community.domain.posts;

import java.util.Date;

import lombok.Data;


@Data
public class PostRequestDTO {
    private int     post_id;
    private String  post_title;
    private String  post_contents;
    private Date    post_date;
    private Date    post_modify_date;
    private String  post_hashtag;
    private int     post_unlike_counts;
    private int     post_like_counts;
    private int     post_views;
    private String  post_up_sport;
    private String  post_sport;



    
    private String  post_sports_keyword;
    private String  post_img1;
    private String  post_img2;


    // 외래키
    private int member_id;
    
    private String post_nickname;
}

