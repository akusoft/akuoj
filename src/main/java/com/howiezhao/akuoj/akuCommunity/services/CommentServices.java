package com.howiezhao.akuoj.akuCommunity.services;


import com.howiezhao.akuoj.akuCommunity.dao.Comment;

import java.util.List;

/**
 * @Author: LiMing
 * @Date: 2020/3/17 14:53
 * @Description:
 **/
public interface CommentServices {

    List<Comment> slelctCommentList(int entityType, int entityId, int offset, int limit);


    int selectCommuntCount(int entityType, int entityId);

    int insertComment(Comment comment);

}
