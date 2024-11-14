package com.multipjt.multi_pjt.community.domain.comments;

import java.util.Date;

import lombok.Data;

@Data
public class CommentResponseDTO {
    private int     comments_id;
    private String  comments_contents;
    private Date    comments_date;
    
    // 외래키 
    private int     member_id;
    private int     post_id;

    // join
    private String c_nickname;

    private boolean isCommentAuthor;

    private boolean comment_like_status;
    private boolean comment_dislike_status;
    private int     comment_like_counts;
    private int     comment_dislike_counts; 

}
